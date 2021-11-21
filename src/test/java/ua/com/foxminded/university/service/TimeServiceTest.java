package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableTimeException;
import ua.com.foxminded.university.exceptions.NotUniqueTimeException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@ContextConfiguration(classes = {AppConfig.class})
public class TimeServiceTest {

    @Mock
    private TimeDao timeDao;
    @InjectMocks
    private TimeService timeService;
    private List<Time> times;
    @Value("${lesson.min.duration}")
    private int minLessonDuration;

    @BeforeEach
    public void setUp() throws IOException {
        ReflectionTestUtils.setField(timeService, "minLessonDuration", minLessonDuration);
        times = new ArrayList<>();
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        time1.setId(1);
        Time time2 = new Time(LocalTime.of(10, 0), LocalTime.of(11, 30));
        time2.setId(2);
        times.add(time1);
        times.add(time2);
    }

    @Test
    public void givenNewTime_whenCreate_thenCreated() throws NotAvailableTimeException, NotUniqueTimeException {
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(13, 30);
        Time time = new Time(start, end);

        when(timeDao.getByTime(start, end)).thenReturn(Optional.empty());
        when(timeDao.getAll()).thenReturn(times);

        timeService.create(time);

        verify(timeDao).create(time);
    }

    @Test
    public void givenExistentTime_whenCreate_thenThrowException() {
        Time time = new Time(times.get(0).getStartTime(), times.get(0).getEndTime());

        when(timeDao.getByTime(time.getStartTime(), time.getEndTime())).thenReturn(Optional.of(times.get(0)));

        Exception exception = assertThrows(NotUniqueTimeException.class, () -> timeService.create(time));

        String expectedMessage = "Time with start = 08:00 and end = 09:30 already exist";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenTimeWithDurationLessThanMinLessDuration_whenCreate_thenThrowException() {
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(12, 20);
        Time time = new Time(start, end);

        when(timeDao.getByTime(start, end)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableTimeException.class, () -> timeService.create(time));

        String expectedMessage = "Duration less than 30 minute(s)";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenTimeThatCrossWithOtherTime_whenCreate_thenThrowException() {
        LocalTime start = LocalTime.of(8, 20);
        LocalTime end = LocalTime.of(9, 20);
        Time time = new Time(start, end);

        when(timeDao.getByTime(start, end)).thenReturn(Optional.empty());
        when(timeDao.getAll()).thenReturn(times);

        Exception exception = assertThrows(NotAvailableTimeException.class, () -> timeService.create(time));

        String expectedMessage = "Time with start = 08:20 and end = 09:20 crossing with other time";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        Time time = times.get(0);

        when(timeDao.getById(1)).thenReturn(Optional.of(time));

        assertEquals(time, timeService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(timeDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> timeService.getById(20));

        String expectedMessage = "Time with id = 20 not found";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() throws NotAvailableTimeException, NotUniqueTimeException {
        Time time = times.get(0);
        time.setStartTime(LocalTime.of(12, 0));
        time.setEndTime(LocalTime.of(13, 30));

        when(timeDao.getByTime(time.getStartTime(), time.getEndTime())).thenReturn(Optional.of(time));
        when(timeDao.getAll()).thenReturn(times);

        timeService.update(time);

        verify(timeDao).update(time);
    }

    @Test
    public void givenTimeWithDurationLessThanMinLessDuration_whenUpdate_thenThrowException() {
        Time time = times.get(0);
        time.setStartTime(LocalTime.of(8, 0));
        time.setEndTime(LocalTime.of(8, 20));

        when(timeDao.getByTime(time.getStartTime(), time.getEndTime())).thenReturn(Optional.of(time));

        Exception exception = assertThrows(NotAvailableTimeException.class, () -> timeService.update(time));

        String expectedMessage = "Duration less than 30 minute(s)";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenTimeThatCrossWithOtherTime_whenUpdate_thenThrowException() {
        LocalTime start = LocalTime.of(8, 20);
        LocalTime end = LocalTime.of(9, 20);
        Time time = times.get(1);
        time.setStartTime(start);
        time.setEndTime(end);

        when(timeDao.getByTime(time.getStartTime(), time.getEndTime())).thenReturn(Optional.of(time));
        when(timeDao.getAll()).thenReturn(times);

        Exception exception = assertThrows(NotAvailableTimeException.class, () -> timeService.update(time));

        String expectedMessage = "Time with start = 08:20 and end = 09:20 crossing with other time";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        timeService.delete(1);

        verify(timeDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        when(timeDao.getAll()).thenReturn(times);

        assertEquals(times, timeService.getAll());
    }

}
