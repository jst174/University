package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.dao.CourseRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Course;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseService courseService;

    @Test
    public void givenNewCourse_whenCreate_thenCreated() throws NotUniqueNameException {
        Course course = new Course("History");
        when(courseRepository.findByName(course.getName())).thenReturn(Optional.empty());

        courseService.create(course);

        verify(courseRepository).save(course);
    }

    @Test
    public void givenCourseWithExistentName_whenCreate_thenNotUniqueNameExceptionThrow() {
        Course course = new Course(TestData.course1.getName());
        when(courseRepository.findByName(course.getName())).thenReturn(Optional.of(TestData.course1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.create(course));

        String expectedMessage = "Course with name = Math already exist";
        verify(courseRepository, never()).save(course);
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(courseRepository.findById(1)).thenReturn(Optional.of(TestData.course1));

        assertEquals(TestData.course1, courseService.getById(1));

    }

    @Test
    public void givenNotExistentCourseId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(courseRepository.findById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> courseService.getById(20));

        String expectedMessage = "Course with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentCourse_whenUpdate_thenUpdated() throws NotUniqueNameException {
        when(courseRepository.findByName(TestData.course1.getName())).thenReturn(Optional.of(TestData.course1));

        courseService.update(TestData.course1);

        verify(courseRepository).save(TestData.course1);
    }

    @Test
    public void givenCourseWithOtherCourseName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Course course1 = TestData.course1;
        Course course2 = TestData.course2;
        course1.setName(course2.getName());
        when(courseRepository.findByName(course1.getName())).thenReturn(Optional.of(course2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.update(course1));

        String expectedMessage = "Course with name = Physics already exist";
        verify(courseRepository, never()).save(course1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentCourse_whenDelete_thenDeleted() {
        courseService.delete(1);

        verify(courseRepository).deleteById(1);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        List<Course> courses = Arrays.asList(TestData.course1, TestData.course2);
        Pageable pageable = PageRequest.of(1, 10);
        Page<Course> coursePage = new PageImpl<Course>(courses, pageable, courses.size());
        when(courseRepository.findAll(pageable)).thenReturn(coursePage);

        assertEquals(coursePage, courseService.getAll(pageable));
    }

    @Test
    public void whenGetAll_thenReturn() {
        List<Course> courses = Arrays.asList(TestData.course1, TestData.course2);
        when(courseRepository.findAll()).thenReturn(courses);

        assertEquals(courses, courseService.getAll());
    }

    interface TestData {
        Course course1 = new Course.Builder()
            .setName("Math")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Physics")
            .setId(2)
            .build();
    }
}
