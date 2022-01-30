package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_holiday_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateHolidayDaoTest {

    @Autowired
    private HolidayDao holidayDao;

    @Test
    public void givenNewHoliday_whenCreate_thenCreated() {
        Holiday holiday = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        holidayDao.create(holiday);

        Holiday actual = holidayDao.getById(3).get();
        assertEquals(holiday, actual);
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Holiday expected = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        Optional<Holiday> actual = holidayDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedHolidayAndId_whenUpdate_thenUpdated() {
        Holiday updatedHoliday = new Holiday("Christmas", LocalDate.of(2022, 1, 7));
        updatedHoliday.setId(1);

        holidayDao.update(updatedHoliday);

        Holiday actual = holidayDao.getById(1).get();
        assertEquals(updatedHoliday, actual);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        holidayDao.delete(1);

        assertEquals(Optional.empty(), holidayDao.getById(1));
    }

    @Test
    public void whenGetAll_thenReturnAllHolidays() {
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2021, 12, 31));
        Holiday holiday2 = new Holiday("Day of knowledge", LocalDate.of(2021, 9, 1));
        List<Holiday> expected = new ArrayList<>();
        expected.add(holiday1);
        expected.add(holiday2);

        List<Holiday> actual = holidayDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturnAllHolidays() {
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2021, 12, 31));
        Holiday holiday2 = new Holiday("Day of knowledge", LocalDate.of(2021, 9, 1));
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday1);
        holidays.add(holiday2);
        Pageable pageable = PageRequest.of(0, holidays.size());
        Page<Holiday> holidayPage = new PageImpl<Holiday>(holidays, pageable, holidays.size());

        assertEquals(holidayPage, holidayDao.getAll(pageable));
    }

    @Test
    public void givenHolidayDate_whenGetByDate_thenReturn() {
        LocalDate date = LocalDate.of(2021, 12, 31);
        Holiday expected = new Holiday("New Year", date);

        Optional<Holiday> actual = holidayDao.getByDate(LocalDate.of(2021, 12, 31));

        assertEquals(expected, actual.get());
    }

    @Test
    public void whenCountTotalRows_thenReturn(){
        assertEquals(2, holidayDao.countTotalRows());
    }
}
