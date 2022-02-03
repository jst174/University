package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_course_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateCourseDaoTest {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewCourse_whenCreate_thenCreated() {
        Course course = new Course("History");

        courseDao.create(course);

        assertEquals(course, hibernateTemplate.get(Course.class, course.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        Course courseExpected = new Course("History");

        assertEquals(courseExpected, courseDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedCourseAndId_whenUpdated_thenUpdated() {
        Course updatedCourse = new Course("Math");
        updatedCourse.setId(1);

        courseDao.update(updatedCourse);

        assertEquals(updatedCourse, hibernateTemplate.get(Course.class, updatedCourse.getId()));

    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        if (hibernateTemplate.get(Course.class, 1) != null) {
            courseDao.delete(1);
            hibernateTemplate.clear();
        }

        assertNull(hibernateTemplate.get(Course.class, 1));
    }

    @Test
    @Transactional
    public void whenGetAll_thenReturnAllCourses() {
        Course course1 = new Course("History");
        Course course2 = new Course("Music");
        List<Course> expected = new ArrayList<>();
        expected.add(course1);
        expected.add(course2);

        List<Course> actual = courseDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void givenPageable_whenGetAll_thenReturnAllCourses() {
        Course course1 = new Course("History");
        Course course2 = new Course("Music");
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Course> coursePage = new PageImpl<Course>(courses, pageable, courses.size());

        assertEquals(coursePage, courseDao.getAll(pageable));
    }

    @Test
    @Transactional
    public void givenCourseName_whereGetByName_thenReturn() {
        Course courseExpected = new Course("History");

        Optional<Course> courseActual = courseDao.getByName("History");

        assertEquals(courseExpected, courseActual.get());
    }

    @Test
    @Transactional
    public void whenCount_whenReturn(){
        assertEquals(2, courseDao.count());
    }
}
