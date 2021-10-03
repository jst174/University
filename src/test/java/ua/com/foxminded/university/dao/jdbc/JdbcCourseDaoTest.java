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
import ua.com.foxminded.university.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.university.model.Course;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_course_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcCourseDaoTest {

    @Autowired
    private JdbcCourseDao courseDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewCourse_whenCreate_thenCreated() {
        Course course = new Course("History");

        courseDao.create(course);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Course courseExpected = new Course("History");

        Course courseActual = courseDao.getById(1);

        assertEquals(courseExpected, courseActual);
    }

    @Test
    public void givenUpdatedCourseAndId_whenUpdated_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM courses WHERE name = 'Math'";
        Course updatedCourse = new Course("Math");

        courseDao.update(1, updatedCourse);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", SQL));

    }

    @Test
    public void givenId_whenDelete_thenDeleted(){
        courseDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
    }

    @Test
    public void whenGetAll_thenReturnAllCourses(){
        Course course1 = new Course("History");
        Course course2 = new Course("Music");
        List<Course> expected= new ArrayList<>();
        expected.add(course1);
        expected.add(course2);

        List<Course> actual = courseDao.getAll();

        assertEquals(expected, actual);
    }
}
