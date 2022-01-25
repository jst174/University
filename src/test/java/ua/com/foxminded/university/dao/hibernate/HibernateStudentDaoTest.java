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
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_groups_test.sql", "/create_student_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateStudentDaoTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws IOException {
        Student student = new Student.Builder().clone(TestData.student1)
            .setLastName("King")
            .setEmail("king97@yandex.ru")
            .setPhoneNumber("3622366")
            .build();
        int expectedRows = countRowsInTable(jdbcTemplate, "students") + 1;

        studentDao.create(student);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        assertEquals(TestData.student1, studentDao.getById(1).get());
    }

    @Test
    public void givenUpdatedStudentAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM students WHERE first_name = 'Mike' and last_name = 'King' and " +
            "birthday = '1997-05-13' and gender = 'MALE' and address_id = 1 and phone_number = '3622366' and email = 'king97@yandex.ru'" +
            "and group_id = 1";
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Group group = new Group("MJ-12");
        group.setId(1);
        Student updatedStudent = new Student(
            "Mike",
            "King",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru"
        );
        updatedStudent.setGroup(group);
        updatedStudent.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "students", sql) + 1;

        studentDao.update(updatedStudent);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "students", sql));

    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "students") - 1;

        studentDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    public void whenGetAll_thenReturnAllStudents() {
        assertEquals(Arrays.asList(TestData.student1, TestData.student2), studentDao.getAll());
    }

    @Test
    public void givenPageable_whenGetAll_thenReturnAllStudents() {
        List<Student> students = Arrays.asList(TestData.student1, TestData.student2);
        Pageable pageable = PageRequest.of(0, students.size());
        Page<Student> studentPage = new PageImpl<Student>(students, pageable, students.size());

        assertEquals(studentPage, studentDao.getAll(pageable));
    }

    @Test
    public void givenGroupId_whenGetByGroupId_thenReturn() {
        assertEquals(Arrays.asList(TestData.student1, TestData.student2), studentDao.getByGroupId(1));
    }

    @Test
    public void givenFirstNameAndLastName_whenGetByName_thenReturn() {
        Student student = TestData.student1;

        Optional<Student> actual = studentDao.getByName(student.getFirstName(), student.getLastName());

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
