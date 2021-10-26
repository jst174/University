package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

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
    public void givenNewTeacher_whenCreate_thenCreated() throws IOException {
        Teacher teacher = dataSource.generateTeacher();

        when(teacherDao.getAll()).thenReturn(teachers);

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    public void givenExistentTeacher_whenCreate_thenThrowException() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> teacherService.create(teacher));

        assertEquals("teacher already exist", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getAll()).thenReturn(teachers);
        when(teacherDao.getById(1)).thenReturn(teacher);

        assertEquals(teacher, teacherService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getById(3));

        assertEquals("teacher is not found", exception.getMessage());
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getAll()).thenReturn(teachers);

        teacherService.update(teacher);

        verify(teacherDao).update(teacher);
    }

    @Test
    public void givenNotExistentTeacher_whenUpdate_thenThrowException() throws IOException {
        Teacher teacher = dataSource.generateTeacher();

        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> teacherService.update(teacher));

        assertEquals("teacher is not found", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        when(teacherDao.getAll()).thenReturn(teachers);

        teacherService.delete(1);

        verify(teacherDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDeleted_thenThrowException() {
        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> teacherService.delete(3));

        assertEquals("teacher is not found", exception.getMessage());
    }
}
