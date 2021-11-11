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
import java.util.Optional;

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
    public void givenNewTeacher_whenCreate_thenCreated() throws IOException {
        Teacher teacher = dataSource.generateTeacher();

        when(teacherDao.getById(teacher.getId())).thenReturn(Optional.empty());
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.empty());

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    public void givenExistentTeacher_whenCreate_thenNotCreated() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getById(teacher.getId())).thenReturn(Optional.empty());
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())).thenReturn(Optional.of(teacher));

        teacherService.create(teacher);

        verify(teacherDao, never()).create(teacher);
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getById(1)).thenReturn(Optional.of(teacher));

        assertEquals(teacher, teacherService.getById(1));
    }

    @Test
    public void givenExistentTime_whenUpdate_thenUpdated() {
        Teacher teacher = teachers.get(0);

        when(teacherDao.getById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(teacherDao.getByName(teacher.getFirstName(), teacher.getLastName()))
            .thenReturn(Optional.of(teacher));

        teacherService.update(teacher);

        verify(teacherDao).update(teacher);
    }

    @Test
    public void givenTeacherWithOtherTeacherName_whenUpdate_thenNotUpdated() {
        Teacher teacher1 = teachers.get(0);
        Teacher teacher2 = teachers.get(1);
        teacher1.setFirstName(teacher2.getFirstName());
        teacher1.setLastName(teacher2.getLastName());

        when(teacherDao.getById(teacher1.getId())).thenReturn(Optional.of(teacher1));
        when(teacherDao.getByName(teacher1.getFirstName(), teacher1.getLastName()))
            .thenReturn(Optional.of(teacher2));

        teacherService.update(teacher1);

        verify(teacherDao, never()).update(teacher1);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        teacherService.delete(1);

        verify(teacherDao).delete(1);
    }
}
