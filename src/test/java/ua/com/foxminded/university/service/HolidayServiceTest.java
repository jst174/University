package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueDateException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void givenNewHoliday_whenCreate_thenCreated() throws NotUniqueDateException {
        LocalDate date = LocalDate.of(2022, 3, 8);
        Holiday holiday = new Holiday("Women's Day", date);
        when(holidayDao.getByDate(holiday.getDate())).thenReturn(Optional.empty());

        holidayService.create(holiday);

        verify(holidayDao).create(holiday);
    }

    @Test
    public void givenHolidayWithOtherHolidayDate_whenCreate_thenNotUniqueDateExceptionThrow() {
        Holiday holiday = new Holiday("holiday", holidays.get(0).getDate());
        when(holidayDao.getByDate(holiday.getDate())).thenReturn(Optional.of(holidays.get(0)));

        Exception exception = assertThrows(NotUniqueDateException.class, () -> holidayService.create(holiday));

        String expectedMessage = "Holiday with date = 2022-01-01 already exist";
        verify(holidayDao, never()).create(holiday);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentHolidayId_whenGetById_thenReturn() throws EntityNotFoundException {
        Holiday holiday = holidays.get(0);
        when(holidayDao.getById(1)).thenReturn(Optional.of(holiday));

        assertEquals(holiday, holidayService.getById(1));
    }

    @Test
    public void givenNotExistentHolidayId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(holidayDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> holidayService.getById(20));

        String expectedMessage = "Holiday with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentHoliday_whenUpdate_thenUpdated() throws NotUniqueDateException {
        Holiday holiday = holidays.get(0);
        when(holidayDao.getByDate(holiday.getDate())).thenReturn(Optional.of(holiday));

        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    public void givenHolidayWithOtherHolidayDate_whenUpdate_thenNotUniqueDateExceptionThrow() {
        Holiday holiday1 = holidays.get(0);
        Holiday holiday2 = holidays.get(1);
        holiday1.setDate(holiday2.getDate());
        when(holidayDao.getByDate(holiday1.getDate())).thenReturn(Optional.of(holiday2));

        Exception exception = assertThrows(NotUniqueDateException.class, () -> holidayService.update(holiday1));

        String expectedMessage = "Holiday with date = 2022-01-07 already exist";
        verify(holidayDao, never()).update(holiday1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentHolidayId_whenDelete_thenDeleted() {
        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Holiday> holidayPage = new PageImpl<Holiday>(holidays, pageable, holidays.size());
        when(holidayDao.getAll(pageable)).thenReturn(holidayPage);

        assertEquals(holidayPage, holidayService.getAll(pageable));
    }

}
