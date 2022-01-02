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
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

    @BeforeEach
    public void setUp() throws IOException {
        ReflectionTestUtils.setField(studentService, "maxGroupSize", maxGroupSize);
        students = Arrays.asList(TestData.student1, TestData.student2);
    }

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws NotAvailableGroupException, NotUniqueNameException {
        when(studentDao.getByGroupId(1)).thenReturn(students);
        when(studentDao.getByName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.empty());

        studentService.create(TestData.student1);

        verify(studentDao).create(TestData.student1);
    }

    @Test
    public void givenExistentStudent_whenCreate_thenNotUniqueNameExceptionThrow() {
        Student student = new Student();
        student.setFirstName(TestData.student1.getFirstName());
        student.setLastName(TestData.student1.getLastName());
        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(TestData.student1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.create(student));

        String expectedMessage = format("Student with name %s %s already exist",
            student.getFirstName(), student.getLastName());
        verify(studentDao, never()).create(student);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenCreate_thenNotAvailableGroupExceptionThrow() {
        when(studentDao.getByName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.create(TestData.student1));

        String expectedMessage = "Group with name NG-12 not available. Max group size = 30 has already been reached";
        verify(studentDao, never()).create(TestData.student1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudentId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(studentDao.getById(1)).thenReturn(Optional.of(TestData.student1));

        assertEquals(TestData.student1, studentService.getById(1));
    }

    @Test
    public void givenNotExistentStudentId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(studentDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.getById(20));

        String expectedMessage = "Student with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudent_whenUpdate_thenUpdated() throws NotAvailableGroupException, NotUniqueNameException, EntityNotFoundException {
        when(studentDao.getByName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.of(TestData.student1));
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.update(TestData.student1);

        verify(studentDao).update(TestData.student1);
    }

    @Test
    public void givenStudentWithOtherStudentName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        when(studentDao.getByName(TestData.student2.getFirstName(), TestData.student2.getLastName())).thenReturn(Optional.of(TestData.student1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.update(TestData.student2));

        String expectedMessage = format("Student with name %s %s already exist",
            TestData.student2.getFirstName(), TestData.student2.getLastName());
        verify(studentDao, never()).update(TestData.student2);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenUpdate_thenNotAvailableGroupExceptionThrow() {
        when(studentDao.getByName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.of(TestData.student1));
        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.update(TestData.student1));

        String expectedMessage = "Group with name NG-12 not available. Max group size = 30 has already been reached";
        verify(studentDao, never()).update(TestData.student1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        studentService.delete(1);

        verify(studentDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Student> studentPage =
            new PageImpl<Student>(students, pageable, students.size());
        when(studentDao.getAll(pageable)).thenReturn(studentPage);

        assertEquals(studentPage, studentService.getAll(pageable));
    }

    @Test
    public void givenGroupId_whenGetByGroupId_thenReturn() {
        when(studentDao.getByGroupId(1)).thenReturn(students);

        assertEquals(students, studentService.getByGroupId(1));
    }

    private List<Student> generateStudents() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < maxGroupSize; i++) {
            students.add(TestData.student1);
        }
        return students;
    }

    interface TestData {
        Group group1 = new Group.Builder()
            .setName("NG-12")
            .setId(1)
            .build();
        Student student1 = new Student.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setGroup(group1)
            .setId(1)
            .build();
        Student student2 = new Student.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setGroup(group1)
            .setId(2)
            .build();
    }
}
