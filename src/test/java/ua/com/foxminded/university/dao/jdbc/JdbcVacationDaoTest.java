package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_vacation_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcVacationDaoTest {

    @Autowired
    public VacationDao vacationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Teacher teacher;

    @BeforeEach
    private void setUp() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        teacher = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
    }

    @Test
    public void givenNewVacation_whenCreate_thenCreated() {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);
        int expectedRows = countRowsInTable(jdbcTemplate, "vacations") + 1;

        vacationDao.create(vacation);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Vacation expected = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);

        Optional<Vacation> actual = vacationDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedVacationAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM vacations WHERE start = '2021-11-15' and ending = '2021-11-30' and teacher_id = 1";
        Vacation updatedVacation = new Vacation(
            LocalDate.of(2021, 11, 15),
            LocalDate.of(2021, 11, 30),
            teacher);
        updatedVacation.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "vacations", sql) + 1;

        vacationDao.update(updatedVacation);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "vacations", sql));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "vacations") - 1;

        vacationDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void whenGetAll_thenReturnAllVacations() {
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 15),
            LocalDate.of(2021, 5, 30),
            teacher);
        List<Vacation> expected = new ArrayList<>();
        expected.add(vacation1);
        expected.add(vacation2);

        List<Vacation> actual = vacationDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenTeacherId_whenGetByTeacherId_thenReturnVacations() {
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 15),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(2);
        List<Vacation> expected = new ArrayList<>();
        expected.add(vacation1);
        expected.add(vacation2);

        List<Vacation> actual = vacationDao.getByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenTeacherAndLessonDate_whenGetByTeacherAndLessonDate_thenReturn(){
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 15),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(2);

        Optional<Vacation> actual = vacationDao.getByTeacherAndLessonDate(teacher,
            LocalDate.of(2021, 10, 20));

        assertEquals(vacation1, actual.get());
    }

    @Test
    public void givenVacation_whenGetByTeacherAndVacationDates_thenReturn(){
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 15),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(2);

        Optional<Vacation> actual = vacationDao.getByTeacherAndVacationDates(vacation1);

        assertEquals(vacation1, actual.get());
    }


}
