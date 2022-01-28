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
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_vacation_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateVacationDaoTest {

    @Autowired
    public VacationDao vacationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewVacation_whenCreate_thenCreated() {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30),
            TestData.teacher);
        int expectedRows = countRowsInTable(jdbcTemplate, "vacations") + 1;

        vacationDao.create(vacation);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        assertEquals(TestData.vacation1, vacationDao.getById(1).get());
    }

    @Test
    public void givenUpdatedVacationAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM vacations WHERE start = '2021-11-15' and ending = '2021-11-30' and teacher_id = 1";
        Vacation updatedVacation = new Vacation(
            LocalDate.of(2021, 11, 15),
            LocalDate.of(2021, 11, 30),
            TestData.teacher);
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
        assertEquals(Arrays.asList(TestData.vacation1, TestData.vacation2), vacationDao.getAll());
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        List<Vacation> vacations = Arrays.asList(TestData.vacation1, TestData.vacation2);
        Pageable pageable = PageRequest.of(0, vacations.size());
        Page<Vacation> vacationPage = new PageImpl<Vacation>(vacations, pageable, vacations.size());

        assertEquals(vacationPage, vacationDao.getAll(pageable));
    }

    @Test
    public void givenTeacherAndLessonDate_whenGetByTeacherAndLessonDate_thenReturn() {
        assertEquals(TestData.vacation1, vacationDao.getByTeacherAndLessonDate(TestData.teacher,
            LocalDate.of(2021, 10, 20)).get());
    }

    @Test
    public void givenVacation_whenGetByTeacherAndVacationDates_thenReturn() {
        assertEquals(TestData.vacation1, vacationDao.getByTeacherAndVacationDates(TestData.vacation1).get());
    }

    interface TestData {
        Address address = new Address.Builder()
            .setCountry("Russia")
            .setCity("Saint Petersburg")
            .setStreet("Nevsky Prospect")
            .setHouseNumber("15")
            .setApartmentNumber("45")
            .setPostcode("342423")
            .setId(1)
            .build();
        Teacher teacher = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1977, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller77@gmail.com")
            .setAcademicDegree(AcademicDegree.MASTER)
            .setId(1)
            .build();
        Vacation vacation1 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 10, 15))
            .setEnd(LocalDate.of(2021, 10, 30))
            .setTeacher(teacher)
            .build();
        Vacation vacation2 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 5, 15))
            .setEnd(LocalDate.of(2021, 5, 30))
            .setTeacher(teacher)
            .build();
    }


}
