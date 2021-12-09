package ua.com.foxminded.university.dao.jdbc;

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
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_teacher_test.sql", "/create_teacher_courses_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;
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
        teacher.setAddress(address);
        Course course1 = new Course("History");
        Course course2 = new Course("Math");
        Course course3 = new Course("Physic");
        course1.setId(1);
        course2.setId(2);
        course3.setId(3);
        teacher.getCourses().add(course1);
        teacher.getCourses().add(course2);
        teacher.getCourses().add(course3);
        int expectedRows = countRowsInTable(jdbcTemplate, "teachers") + 1;
        int expectedCoursesRows = countRowsInTable(jdbcTemplate, "teachers_courses") + 3;

        teacherDao.create(teacher);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "teachers"));
        assertEquals(expectedCoursesRows, countRowsInTable(jdbcTemplate, "teachers_courses"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        assertEquals(TestData.teacher1, teacherDao.getById(1).get());
    }

    @Test
    public void givenUpdatedTeacherAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM teachers WHERE first_name = 'Alan' and last_name = 'King' and " +
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
        Course course1 = new Course("History");
        course1.setId(1);
        Course course2 = new Course("Art");
        course2.setId(4);
        updatedTeacher.getCourses().add(course1);
        updatedTeacher.getCourses().add(course2);
        updatedTeacher.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "teachers", sql) + 1;
        int expectedCoursesRows = countRowsInTable(jdbcTemplate, "teachers_courses") - 1;

        teacherDao.update(updatedTeacher);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "teachers", sql));
        assertEquals(expectedCoursesRows, countRowsInTable(jdbcTemplate, "teachers_courses"));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "teachers") - 1;

        teacherDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void whenGetAll_thenReturnAllTeachers() {
        assertEquals(Arrays.asList(TestData.teacher1, TestData.teacher2), teacherDao.getAll());
    }

    @Test
    public void givenPageable_whenGetAll_thenReturnAllTeachers() {
        List<Teacher> teachers = Arrays.asList(TestData.teacher1, TestData.teacher2);
        Pageable pageable = PageRequest.of(0, teachers.size());
        Page<Teacher> teacherPage = new PageImpl<Teacher>(teachers, pageable, teachers.size());

        assertEquals(teacherPage, teacherDao.getAll(pageable));
    }

    @Test
    public void givenFirstNameAndLastName_whenGetByName_thenReturn() {
        Teacher expected = TestData.teacher1;

        Optional<Teacher> actual = teacherDao.getByName(expected.getFirstName(), expected.getLastName());

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
            .setId(2)
            .build();
    }

}
