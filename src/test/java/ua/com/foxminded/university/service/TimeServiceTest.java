package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    @Mock
    private TimeDao timeDao;
    @InjectMocks
    private TimeService timeService;
    private List<Time> times;

    @BeforeEach
    public void setUp() throws IOException {
        times = new ArrayList<>();
        Time time1 = new Time(LocalTime.of(8,0), LocalTime.of(9,30));
        time1.setId(1);
        Time time2 = new Time(LocalTime.of(10,0), LocalTime.of(11,30));
        time2.setId(2);
        times.add(time1);
        times.add(time2);
    }

    @Test
    public void givenNewTime_whenCreate_thenCreated() throws IOException {
        Time time = new Time(LocalTime.of(12,0), LocalTime.of(13,30));

        when(timeDao.getAll()).thenReturn(times);

        timeService.create(time);

        verify(timeDao).create(time);
    }

    @Test
    public void givenExistentTime_whenCreate_thenThrowException() {
        Time time = times.get(0);

        when(timeDao.getAll()).thenReturn(times);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> timeService.create(time));

        assertEquals("time already exist", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Time time = times.get(0);

        when(timeDao.getAll()).thenReturn(times);
        when(timeDao.getById(1)).thenReturn(time);

        assertEquals(time, timeService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(timeDao.getAll()).thenReturn(times);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> timeService.getById(3));

        assertEquals("time is not found", exception.getMessage());
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() {
        Time time = times.get(0);

        when(timeDao.getAll()).thenReturn(times);

        timeService.update(time);

        verify(timeDao).update(time);
    }

    @Test
    public void givenNotExistentTime_whenUpdate_thenThrowException() throws IOException {
        Time time = new Time(LocalTime.of(12,0), LocalTime.of(13,30));

        when(timeDao.getAll()).thenReturn(times);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> timeService.update(time));

        assertEquals("time is not found", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        when(timeDao.getAll()).thenReturn(times);

        timeService.delete(1);

        verify(timeDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDeleted_thenThrowException() {
        when(timeDao.getAll()).thenReturn(times);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> timeService.delete(3));

        assertEquals("time is not found", exception.getMessage());
    }
}
