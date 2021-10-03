package ua.com.foxminded.university.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.SpringConfigTest;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_holiday_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcHolidayDaoTest {

    @Autowired
    private JdbcHolidayDao holidayDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewHoliday_whenCreate_thenCreated() {
        Holiday holiday = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        holidayDao.create(holiday);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Holiday expected = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        Holiday actual = holidayDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedHolidayAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM holidays WHERE name = 'Christmas' and date = '2022-01-07'";
        Holiday updatedHoliday = new Holiday("Christmas", LocalDate.of(2022, 01, 07));

        holidayDao.update(1, updatedHoliday);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted(){
        holidayDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays"));
    }

    @Test
    public void whenGetAll_thenReturnAllHolidays(){
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2021, 12, 31));
        Holiday holiday2 = new Holiday("Day of knowledge", LocalDate.of(2021, 9, 1));
        List<Holiday> expected = new ArrayList<>();
        expected.add(holiday1);
        expected.add(holiday2);

        List<Holiday> actual = holidayDao.getAll();

        assertEquals(expected, actual);
    }
}