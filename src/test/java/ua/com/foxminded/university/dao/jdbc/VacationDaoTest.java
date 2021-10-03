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
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_vacation_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VacationDaoTest {

    @Autowired
    public JdbcVacationDao vacationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewVacation_whenCreate_thenCreated() {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30));

        vacationDao.create(vacation);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Vacation expected = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30));

        Vacation actual = vacationDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedVacationAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM vacations WHERE start = '2021-11-15' and ending = '2021-11-30'";
        Vacation updatedVacation = new Vacation(
            LocalDate.of(2021, 11, 15),
            LocalDate.of(2021, 11, 30));

        vacationDao.update(1, updatedVacation);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        vacationDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void whenGetAll_thenReturnAllVacations(){
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30));
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 15),
            LocalDate.of(2021, 5, 30));
        List<Vacation> expected = new ArrayList<>();
        expected.add(vacation1);
        expected.add(vacation2);

        List<Vacation> actual = vacationDao.getAll();

        assertEquals(expected, actual);
    }


}
