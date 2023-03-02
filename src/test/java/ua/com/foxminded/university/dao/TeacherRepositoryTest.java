package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_address_test.sql", "/create_teacher_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void givenFirstNameAndLastName_whenGetByFirstNameAndLastName_thenReturn() {
        Teacher expected = TestData.teacher1;

        Optional<Teacher> actual = teacherRepository.findByFirstNameAndLastName(expected.getFirstName(), expected.getLastName());

        assertEquals(expected, actual.get());
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
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1977, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller77@gmail.com")
            .setAcademicDegree(AcademicDegree.MASTER)
            .setCourses(new ArrayList<>())
            .setVacations(new ArrayList<>())
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Bob")
            .setLastName("King")
            .setBirtDate(LocalDate.of(1965, 11, 21))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5345345")
            .setEmail("king65@gmail.com")
            .setAcademicDegree(AcademicDegree.DOCTORAL)
            .setCourses(new ArrayList<>())
            .setVacations(new ArrayList<>())
            .setId(2)
            .build();
    }

}
