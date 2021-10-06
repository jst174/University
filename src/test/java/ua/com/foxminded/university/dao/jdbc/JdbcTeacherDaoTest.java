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
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_teacher_test.sql", "/create_teacher_courses_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTeacherDaoTest {

    @Autowired
    private JdbcTeacherDao teacherDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewTeacher_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        Teacher teacher = new Teacher(
            "Alex",
            "King",
            LocalDate.of(1977, 12, 16),
            Gender.MALE,
            address,
            "36d22366",
            "king97@yandex.ru",
            AcademicDegree.MASTER
        );
        address.setId(1);
        teacher.setAdress(address);

        teacherDao.create(teacher);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Teacher expected = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );

        Teacher actual = teacherDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedTeacherAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM teachers WHERE first_name = 'Alan' and last_name = 'King' and " +
            "birthday = '1945-12-16' and gender = 'MALE' and address_id = 1 and phone_number = '3622366' " +
            "and email = 'king97@yandex.ru' and academic_degree = 'DOCTORAL'";
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher updatedTeacher = new Teacher(
            "Alan",
            "King",
            LocalDate.of(1945, 12, 16),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru",
            AcademicDegree.DOCTORAL
        );
        updatedTeacher.setId(1);

        teacherDao.update(updatedTeacher);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        teacherDao.delete(1);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void givenTeacherIdAndCourseId_whenAddCourse_thenAddCourseToTeacher() {
        teacherDao.addCourse(1, 1);
        teacherDao.addCourse(1, 2);
        teacherDao.addCourse(1, 3);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_courses"));
    }

    @Test
    public void whenGetAll_thenReturnAllTeachers(){
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher teacher = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );
        List<Teacher> expected = new ArrayList<>();
        expected.add(teacher);

        List<Teacher> actual = teacherDao.getAll();

        assertEquals(expected, actual);
    }
}
