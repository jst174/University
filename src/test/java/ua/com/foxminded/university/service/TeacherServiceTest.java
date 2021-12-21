package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Teacher;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<Teacher> teachers;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        teachers = new ArrayList<>();
        dataSource = new DataSource();
        Teacher teacher1 = dataSource.generateTeacher();
        teacher1.setId(1);
        Teacher teacher2 = dataSource.generateTeacher();
        teacher2.setId(2);
        teachers.add(teacher1);
        teachers.add(teacher2);
    }

    @Test
    public void givenNewTeacher_whenCreate_thenCreated() throws NotUniqueNameException {
        Teacher teacher = dataSource.generateTeacher();
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.empty());

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    public void givenExistentTeacher_whenCreate_thenNotUniqueNameExceptionThrow() {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teachers.get(0).getFirstName());
        teacher.setLastName(teachers.get(0).getLastName());
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.of(teacher));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> teacherService.create(teachers.get(0)));

        String expectedMessage = format("Teacher with name %s %s already exist",
            teacher.getFirstName(), teacher.getLastName());
        verify(teacherDao, never()).create(teacher);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getById(1)).thenReturn(Optional.of(teacher));

        assertEquals(teacher, teacherService.getById(1));
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
        Teacher teacher = teachers.get(0);
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName()))
            .thenReturn(Optional.of(teacher));

        teacherService.update(teacher);

        verify(teacherDao).update(teacher);
    }

    @Test
    public void givenTeacherWithOtherTeacherName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Teacher teacher1 = teachers.get(0);
        Teacher teacher2 = teachers.get(1);
        teacher1.setFirstName(teacher2.getFirstName());
        teacher1.setLastName(teacher2.getLastName());
        when(teacherDao.getByName(teacher1.getFirstName(), teacher1.getLastName()))
            .thenReturn(Optional.of(teacher2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> teacherService.update(teacher1));

        String expectedMessage = format("Teacher with name %s %s already exist",
            teacher1.getFirstName(), teacher1.getLastName());
        verify(teacherDao, never()).update(teacher1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        teacherService.delete(1);

        verify(teacherDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Teacher> teacherPage =
            new PageImpl<Teacher>(teachers, pageable, teachers.size());
        when(teacherDao.getAll(pageable)).thenReturn(teacherPage);

        assertEquals(teacherPage, teacherService.getAll(pageable));
    }
}
