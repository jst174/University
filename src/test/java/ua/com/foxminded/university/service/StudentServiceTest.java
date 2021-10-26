package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;
    private List<Student> students;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        students = new ArrayList<>();
        dataSource = new DataSource();
        Student student1 = dataSource.generateStudent();
        student1.setId(1);
        Student student2 = dataSource.generateStudent();
        student2.setId(2);
        students.add(student1);
        students.add(student2);
    }

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws IOException {
        Student student = dataSource.generateStudent();

        when(studentDao.getAll()).thenReturn(students);

        studentService.create(student);

        verify(studentDao).create(student);
    }

    @Test
    public void givenExistentStudent_whenCreate_thenThrowException() {
        Student student = students.get(0);

        when(studentDao.getAll()).thenReturn(students);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> studentService.create(student));

        assertEquals("student already exist", exception.getMessage());
    }

    @Test
    public void givenExistentStudentId_whenGetById_thenReturn() {
        Student student = students.get(0);

        when(studentDao.getAll()).thenReturn(students);
        when(studentDao.getById(1)).thenReturn(student);

        assertEquals(student, studentService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(studentDao.getAll()).thenReturn(students);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> studentService.getById(3));

        assertEquals("student is not found", exception.getMessage());
    }

    @Test
    public void givenExistentStudent_whenUpdate_thenUpdated() {
        Student student = students.get(0);

        when(studentDao.getAll()).thenReturn(students);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    public void givenNotExistentStudent_whenUpdate_thenThrowException() throws IOException {
        Student student = dataSource.generateStudent();

        when(studentDao.getAll()).thenReturn(students);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> studentService.update(student));

        assertEquals("student is not found", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        when(studentDao.getAll()).thenReturn(students);

        studentService.delete(1);

        verify(studentDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDeleted_thenThrowException() {
        when(studentDao.getAll()).thenReturn(students);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> studentService.delete(3));

        assertEquals("student is not found", exception.getMessage());
    }
}
