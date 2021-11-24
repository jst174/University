package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
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
    public void givenNewCourse_whenCreate_thenCreated() throws NotUniqueNameException {
        Course course = new Course("History");
        when(courseDao.getByName(course.getName())).thenReturn(Optional.empty());

        courseService.create(course);

        verify(courseDao).create(course);
    }

    @Test
    public void givenCourseWithExistentName_whenCreate_thenNotUniqueNameExceptionThrow() {
        Course course = new Course(courses.get(0).getName());
        when(courseDao.getByName(course.getName())).thenReturn(Optional.of(courses.get(0)));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.create(course));

        String expectedMessage = "Course with name = Math already exist";
        verify(courseDao, never()).create(course);
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        Course course = courses.get(0);

        when(courseDao.getById(1)).thenReturn(Optional.of(course));

        assertEquals(course, courseService.getById(1));

    }

    @Test
    public void givenNotExistentCourseId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(courseDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> courseService.getById(20));

        String expectedMessage = "Course with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentCourse_whenUpdate_thenUpdated() throws NotUniqueNameException {
        Course course = courses.get(0);
        when(courseDao.getByName(course.getName())).thenReturn(Optional.of(course));

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    public void givenCourseWithOtherCourseName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Course course1 = courses.get(0);
        Course course2 = courses.get(1);
        course1.setName(course2.getName());
        when(courseDao.getByName(course1.getName())).thenReturn(Optional.of(course2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.update(course1));

        String expectedMessage = "Course with name = Physics already exist";
        verify(courseDao, never()).update(course1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentCourse_whenDelete_thenDeleted() {
        courseService.delete(1);

        verify(courseDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        when(courseDao.getAll()).thenReturn(courses);

        assertEquals(courses, courseService.getAll());
    }

    @Test
    public void givenExistentTeacherId_whenGetByTeacherId_whenReturn() {
        when(courseDao.getByTeacherId(1)).thenReturn(courses);

        assertEquals(courses, courseService.getByTeacherId(1));
    }
}
