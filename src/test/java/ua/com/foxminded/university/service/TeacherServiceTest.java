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
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void givenNewTeacher_whenCreate_thenCreated() throws NotUniqueNameException {
        Teacher teacher = TestData.teacher1;
        when(teacherDao.getByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.empty());

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    public void givenExistentTeacher_whenCreate_thenNotUniqueNameExceptionThrow() {
        Teacher teacher = new Teacher();
        teacher.setFirstName(TestData.teacher1.getFirstName());
        teacher.setLastName(TestData.teacher1.getLastName());
        when(teacherDao.getByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.of(TestData.teacher1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> teacherService.create(teacher));

        String expectedMessage = format("Teacher with name %s %s already exist",
            teacher.getFirstName(), teacher.getLastName());
        verify(teacherDao, never()).create(teacher);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(teacherDao.getById(1)).thenReturn(Optional.of(TestData.teacher1));

        assertEquals(TestData.teacher1, teacherService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(teacherDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.getById(20));

        String expectedMessage = "Teacher with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() throws NotUniqueNameException, EntityNotFoundException {
        when(teacherDao.getByFirstNameAndLastName(TestData.teacher1.getFirstName(), TestData.teacher1.getLastName()))
            .thenReturn(Optional.of(TestData.teacher1));

        teacherService.update(TestData.teacher1);

        verify(teacherDao).update(TestData.teacher1);
    }

    @Test
    public void givenTeacherWithOtherTeacherName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        when(teacherDao.getByFirstNameAndLastName(TestData.teacher2.getFirstName(), TestData.teacher2.getLastName()))
            .thenReturn(Optional.of(TestData.teacher1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> teacherService.update(TestData.teacher2));

        String expectedMessage = format("Teacher with name %s %s already exist",
            TestData.teacher2.getFirstName(), TestData.teacher2.getLastName());
        verify(teacherDao, never()).update(TestData.teacher2);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        teacherService.delete(1);

        verify(teacherDao).delete(1);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        List<Teacher> teachers = Arrays.asList(TestData.teacher1, TestData.teacher2);
        Pageable pageable = PageRequest.of(1, 10);
        Page<Teacher> teacherPage =
            new PageImpl<Teacher>(teachers, pageable, teachers.size());
        when(teacherDao.getAll(pageable)).thenReturn(teacherPage);

        assertEquals(teacherPage, teacherService.getAll(pageable));
    }

    @Test
    public void whenGetAll_thenReturn() {
        List<Teacher> teachers = Arrays.asList(TestData.teacher1, TestData.teacher2);
        when(teacherDao.getAll()).thenReturn(teachers);

        assertEquals(teachers, teacherService.getAll());
    }

    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
            .build();
    }
}
