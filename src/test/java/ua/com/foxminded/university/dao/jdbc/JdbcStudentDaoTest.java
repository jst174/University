package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_groups_test.sql", "/create_student_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcStudentDaoTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws IOException {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Student student = new Student(
            "Mike",
            "King",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru"
        );
        Group group = new Group("MJ-12");
        group.setId(1);
        student.setGroup(group);
        student.setAddress(address);
        int expectedRows = countRowsInTable(jdbcTemplate, "students") + 1;

        studentDao.create(student);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Group group = new Group("MJ-12");
        Student expected = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        expected.setGroup(group);

        Optional<Student> actual = studentDao.getById(1);

        assertEquals(expected, actual.get());
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
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Student student1 = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        Student student2 = new Student(
            "Steve",
            "King",
            LocalDate.of(1995, 5, 2),
            Gender.MALE,
            address,
            "432423432",
            "king95@gmail.com"
        );
        Group group = new Group("MJ-12");
        student1.setGroup(group);
        student2.setGroup(group);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);

        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenGroupId_whenGetByGroupId_thenReturn(){
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Student student1 = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        Student student2 = new Student(
            "Steve",
            "King",
            LocalDate.of(1995, 5, 2),
            Gender.MALE,
            address,
            "432423432",
            "king95@gmail.com"
        );
        Group group = new Group("MJ-12");
        student1.setGroup(group);
        student2.setGroup(group);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);

        List<Student> actual = studentDao.getByGroupId(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenFirstNameAndLastName_whenGetByName_thenReturn() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Group group = new Group("MJ-12");
        Student expected = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        expected.setGroup(group);

        Optional<Student> actual = studentDao.getByName(expected.getFirstName(), expected.getLastName());

        assertEquals(expected, actual.get());
    }
}
