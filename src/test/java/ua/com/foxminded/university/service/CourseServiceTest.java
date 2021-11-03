package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseDao courseDao;
    @InjectMocks
    private CourseService courseService;
    private List<Course> courses;
    private List<Teacher> teachers;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        DataSource dataSource = new DataSource();
        courses = new ArrayList<>();
        Course course1 = new Course("Math");
        course1.setId(1);
        Course course2 = new Course("Physics");
        course2.setId(2);
        courses.add(course1);
        courses.add(course2);
        teachers = new ArrayList<>();
        Teacher teacher1 = dataSource.generateTeacher();
        teacher1.setId(1);
        teacher1.setCourses(courses);
        Teacher teacher2 = dataSource.generateTeacher();
        teacher2.setId(2);
        teachers.add(teacher1);
        teachers.add(teacher2);
    }

    @Test
    public void givenNewCourse_whenCreate_thenCreated() {
        Course course = new Course("History");

        when(courseDao.getByName(course.getName())).thenReturn(Optional.empty());

        courseService.create(course);

        verify(courseDao).create(course);
    }


    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Course course = courses.get(0);

        when(courseDao.getById(1)).thenReturn(Optional.of(course));

        assertEquals(course, courseService.getById(1));

    }

    @Test
    public void givenExistentCourse_whenUpdate_thenUpdated() {
        Course course = courses.get(0);

        when(courseDao.getByName(course.getName())).thenReturn(Optional.of(course));

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    public void givenExistentCourse_whenDelete_thenDeleted() {
        courseService.delete(1);

        verify(courseDao).delete(1);
    }

    @Test
    public void givenExistentTeacherId_whenGetByTeacherId_whenReturn() {
        when(courseDao.getByTeacherId(1)).thenReturn(courses);

        assertEquals(courses, courseService.getByTeacherId(1));
    }
}
