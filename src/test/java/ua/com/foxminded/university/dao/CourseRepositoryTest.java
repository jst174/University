package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Course;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_course_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void givenCourseName_whereGetByName_thenReturn() {
        Course courseExpected = new Course("History");

        Optional<Course> courseActual = courseRepository.findByName("History");

        assertEquals(courseExpected, courseActual.get());
    }
}
