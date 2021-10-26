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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseDao courseDao;
    @Mock
    private TeacherDao teacherDao;
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

        when(courseDao.getAll()).thenReturn(courses);

        courseService.create(course);

        verify(courseDao).create(course);
    }

    @Test
    public void givenExistentCourse_whenCreate_thenThrowException() {
        Course course = courses.get(0);

        when(courseDao.getAll()).thenReturn(courses);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> courseService.create(course));

        assertEquals("course with this name already exist", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Course course = courses.get(0);

        when(courseDao.getAll()).thenReturn(courses);
        when(courseDao.getById(1)).thenReturn(course);

        assertEquals(course, courseService.getById(1));

    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(courseDao.getAll()).thenReturn(courses);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> courseService.getById(3));

        assertEquals("course is not found", exception.getMessage());
    }

    @Test
    public void givenExistentCourse_whenUpdate_thenUpdated() {
        Course course = courses.get(0);

        when(courseDao.getAll()).thenReturn(courses);

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    public void givenNotExistentCourse_whenUpdate_thenThrowException() {
        Course course = new Course("Biology");

        when(courseDao.getAll()).thenReturn(courses);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> courseService.update(course));

        assertEquals("course is not found", exception.getMessage());

    }

    @Test
    public void givenExistentCourse_whenDelete_thenDeleted() {
        when(courseDao.getAll()).thenReturn(courses);

        courseService.delete(1);

        verify(courseDao).delete(1);
    }

    @Test
    public void givenNotExistentCourse_whenDelete_thenThrowException() {
        when(courseDao.getAll()).thenReturn(courses);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> courseService.delete(3));

        assertEquals("course is not found", exception.getMessage());
    }

    @Test
    public void givenExistentTeacherId_whenGetByTeacherId_whenReturn() {
        when(teacherDao.getAll()).thenReturn(teachers);
        when(courseDao.getByTeacherId(1)).thenReturn(courses);

        assertEquals(courses, courseService.getByTeacherId(1));
    }

    @Test
    public void givenNotExistentTeacherId_whenGetByTeacherId_thenThrowException() {
        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> courseService.getByTeacherId(3));

        assertEquals("teacher is not found", exception.getMessage());
    }
}
