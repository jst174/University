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
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_groups_test.sql", "/create_student_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcStudentDaoTest {

    @Autowired
    private JdbcStudentDao studentDao;
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
        address.setId(1);
        student.setAdress(address);

        studentDao.create(student);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
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

        Student actual = studentDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedStudentAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM students WHERE first_name = 'Mike' and last_name = 'King' and " +
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

        studentDao.update(updatedStudent);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", SQL));

    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        studentDao.delete(1);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    public void whenGetAll_thenReturnAllStudents() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Student student = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        student.setGroup(new Group("MJ-12"));
        List<Student> expected = new ArrayList<>();
        expected.add(student);

        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }
}
