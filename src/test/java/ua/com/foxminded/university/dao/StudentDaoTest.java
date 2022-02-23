package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_address_test.sql", "/create_groups_test.sql", "/create_student_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentDaoTest {

    @Autowired
    private StudentDao studentDao;

    @Test
    public void givenFirstNameAndLastName_whenGetByFirstNameAndLastName_thenReturn() {
        Student student = TestData.student1;

        Optional<Student> actual = studentDao.findByFirstNameAndLastName(student.getFirstName(), student.getLastName());

        assertEquals(student, actual.get());
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
        Group group = new Group.Builder()
            .setName("MJ-12")
            .build();
        Student student1 = new Student.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1997, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller97@gmail.com")
            .setGroup(group)
            .build();
        Student student2 = new Student.Builder()
            .setFirstName("Steve")
            .setLastName("King")
            .setBirtDate(LocalDate.of(1995, 5, 2))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("432423432")
            .setEmail("king95@gmail.com")
            .setGroup(group)
            .build();

    }
}
