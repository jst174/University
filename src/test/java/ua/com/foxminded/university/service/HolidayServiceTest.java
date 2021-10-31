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
        LocalDate date = LocalDate.of(2022, 3, 8);
        Holiday holiday = new Holiday("Women's Day", date);

        when(holidayDao.getByDate(date)).thenReturn(holiday);

        holidayService.create(holiday);

        verify(holidayDao).create(holiday);
    }

    @Test
    public void givenExistentHolidayId_whenGetById_thenReturn() {
        Holiday holiday = holidays.get(0);

        when(holidayDao.getById(1)).thenReturn(holiday);

        assertEquals(holiday, holidayService.getById(1));
    }

    @Test
    public void givenExistentHoliday_whenUpdate_thenUpdated() {
        Holiday holiday = holidays.get(0);

        when(holidayDao.getById(1)).thenReturn(holiday);

        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    public void givenExistentHolidayId_whenDelete_thenDeleted() {
        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }

}
