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
import ua.com.foxminded.university.config.SpringConfigTest;
import ua.com.foxminded.university.dao.jdbc.JdbcLessonDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_lesson_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLessonDaoTest {

    @Autowired
    private JdbcLessonDao lessonDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewLesson_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
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
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Lesson lesson = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );

        lessonDao.create(lesson);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher teacher = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 05, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Lesson expected = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );

        Lesson actual = lessonDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        lessonDao.delete(1);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
    }

    @Test
    public void whenGetAll_thenReturnAllLessons(){
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher teacher = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 05, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Lesson lesson = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson);

        List<Lesson> actual = lessonDao.getAll();

        assertEquals(expected, actual);
    }
}
