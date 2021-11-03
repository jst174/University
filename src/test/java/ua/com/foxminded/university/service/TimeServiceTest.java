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
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

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
    public void givenNewTime_whenCreate_thenCreated() throws IOException {
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(13, 30);
        Time time = new Time(start, end);

        when(timeDao.getByTime(start, end)).thenReturn(Optional.empty());

        timeService.create(time);

        verify(timeDao).create(time);
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Time time = times.get(0);

        when(timeDao.getById(1)).thenReturn(Optional.of(time));

        assertEquals(time, timeService.getById(1));
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() {
        Time time = times.get(0);

        when(timeDao.getById(1)).thenReturn(Optional.of(time));

        timeService.update(time);

        verify(timeDao).update(time);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        timeService.delete(1);

        verify(timeDao).delete(1);
    }

}
