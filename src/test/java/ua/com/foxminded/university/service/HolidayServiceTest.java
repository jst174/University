package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private HolidayService holidayService;
    private List<Holiday> holidays;

    @BeforeEach
    public void setUp() {
        holidays = new ArrayList<>();
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2022, 1, 1));
        holiday1.setId(1);
        Holiday holiday2 = new Holiday("Christmas", LocalDate.of(2022, 1, 7));
        holiday2.setId(2);
        holidays.add(holiday1);
        holidays.add(holiday2);
    }

    @Test
    public void givenNewHoliday_whenCreate_thenCreated() {
        Holiday holiday = new Holiday("Women's Day", LocalDate.of(2022, 3, 8));

        when(holidayDao.getAll()).thenReturn(holidays);

        holidayService.create(holiday);

        verify(holidayDao).create(holiday);
    }

    @Test
    public void givenExistentHoliday_whenCreate_thenThrowException() {
        Holiday holiday = holidays.get(0);

        when(holidayDao.getAll()).thenReturn(holidays);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> holidayService.create(holiday));

        assertEquals("holiday already exist", exception.getMessage());
    }

    @Test
    public void givenExistentHolidayId_whenGetById_thenReturn() {
        Holiday holiday = holidays.get(0);

        when(holidayDao.getAll()).thenReturn(holidays);
        when(holidayDao.getById(1)).thenReturn(holiday);

        assertEquals(holiday, holidayService.getById(1));
    }

    @Test
    public void givenNotExistentHolidayId_whenGetById_thenThrowException() {
        when(holidayDao.getAll()).thenReturn(holidays);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> holidayService.getById(3));

        assertEquals("holiday is not found", exception.getMessage());
    }

    @Test
    public void givenExistentHoliday_whenUpdate_thenUpdated() {
        Holiday holiday = holidays.get(0);

        when(holidayDao.getAll()).thenReturn(holidays);

        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    public void givenNotExistentHolidayId_whenUpdate_thenThrowException() {
        Holiday holiday = new Holiday("Women's Day", LocalDate.of(2022, 3, 8));

        when(holidayDao.getAll()).thenReturn(holidays);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> holidayService.update(holiday));

        assertEquals("holiday is not found", exception.getMessage());
    }

    @Test
    public void givenExistentHolidayId_whenDelete_thenDeleted() {
        when(holidayDao.getAll()).thenReturn(holidays);

        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDelete_thenThrowException() {
        when(holidayDao.getAll()).thenReturn(holidays);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> holidayService.delete(3));

        assertEquals("holiday is not found", exception.getMessage());
    }

}
