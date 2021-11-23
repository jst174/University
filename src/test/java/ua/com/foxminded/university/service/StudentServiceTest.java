package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    private static final int maxGroupSize = 30;

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;
    private List<Student> students;
    private DataSource dataSource;
    private Group group;


    @BeforeEach
    public void setUp() throws IOException {
        ReflectionTestUtils.setField(studentService, "maxGroupSize", maxGroupSize);
        students = new ArrayList<>();
        dataSource = new DataSource();
        group = new Group("NG-12");
        group.setId(1);
        Student student1 = dataSource.generateStudent();
        student1.setId(1);
        student1.setGroup(group);
        Student student2 = dataSource.generateStudent();
        student2.setId(2);
        student2.setGroup(group);
        students.add(student1);
        students.add(student2);
    }

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws IOException, NotAvailableGroupException, NotUniqueNameException {
        Student student = dataSource.generateStudent();
        student.setGroup(group);

        when(studentDao.getByGroupId(1)).thenReturn(students);
        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.empty());

        studentService.create(student);

        verify(studentDao).create(student);
    }

    @Test
    public void givenExistentStudent_whenCreate_thenNotUniqueNameExceptionThrow() {
        Student student = new Student();
        student.setFirstName(students.get(0).getFirstName());
        student.setLastName(students.get(0).getLastName());

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.create(students.get(0)));

        String expectedMessage = format("Student with name %s %s already exist",
            student.getFirstName(), student.getLastName());

        verify(studentDao, never()).create(student);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenCreate_thenNotAvailableGroupExceptionThrow() throws IOException {
        Student student = dataSource.generateStudent();
        student.setGroup(group);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.create(student));

        String expectedMessage = "Group with name NG-12 not available. Group is fully filled, size = 30";

        verify(studentDao, never()).create(student);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudentId_whenGetById_thenReturn() throws EntityNotFoundException {
        Student student = students.get(0);

        when(studentDao.getById(1)).thenReturn(Optional.of(student));

        assertEquals(student, studentService.getById(1));
    }

    @Test
    public void givenNotExistentStudentId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(studentDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.getById(20));

        String expectedMessage = "Student with id = 20 not found";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudent_whenUpdate_thenUpdated() throws NotAvailableGroupException, NotUniqueNameException {
        Student student = students.get(0);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    public void givenStudentWithOtherStudentName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Student student1 = students.get(0);
        Student student2 = students.get(1);
        student1.setFirstName(student2.getFirstName());
        student1.setLastName(student2.getLastName());

        when(studentDao.getByName(student1.getFirstName(), student1.getLastName())).thenReturn(Optional.of(student2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.update(student1));

        String expectedMessage = format("Student with name %s %s already exist",
            student1.getFirstName(), student1.getLastName());

        verify(studentDao, never()).update(student1);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenUpdate_thenNotAvailableGroupExceptionThrow() throws IOException {
        Student student = students.get(0);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));
        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.update(student));

        String expectedMessage = "Group with name NG-12 not available. Group is fully filled, size = 30";

        verify(studentDao, never()).update(student);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        studentService.delete(1);

        verify(studentDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        when(studentDao.getAll()).thenReturn(students);

        assertEquals(students, studentService.getAll());
    }

    private List<Student> generateStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < maxGroupSize; i++) {
            students.add(dataSource.generateStudent());
        }
        return students;
    }
}
