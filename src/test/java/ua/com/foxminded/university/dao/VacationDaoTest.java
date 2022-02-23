package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_vacation_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VacationDaoTest {

    @Autowired
    private VacationDao vacationDao;

    @Test
    public void givenTeacherAndDate_whenGetByTeacherAndDate_thenReturn() {
        assertEquals(TestData.vacation1, vacationDao.findByTeacherAndDate(TestData.teacher.getId(),
            LocalDate.of(2021, 10, 20)).get());
    }

    @Test
    public void givenVacation_whenGetByTeacherAndVacationDates_thenReturn() {
        assertEquals(TestData.vacation1, vacationDao.findByTeacherAndVacationDates(TestData.teacher.getId(),
            TestData.vacation1.getStart(), TestData.vacation1.getEnding()).get());
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
