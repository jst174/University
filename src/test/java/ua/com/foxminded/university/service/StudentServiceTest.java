package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.config.UniversityConfigProperties;

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

    @Mock
    private UniversityConfigProperties universityProperties;
    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;
    private List<Student> students;

    @BeforeEach
    public void setUp() throws IOException {
        students = Arrays.asList(TestData.student1, TestData.student2);
    }

    @Test
    public void givenNewStudent_whenCreate_thenCreated() throws NotAvailableGroupException, NotUniqueNameException {
        when(universityProperties.getMaxGroupSize()).thenReturn(30);
        when(studentDao.findByFirstNameAndLastName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.empty());
        TestData.group1.setStudents(Arrays.asList(TestData.student1, TestData.student2));

        studentService.create(TestData.student1);

        verify(studentDao).save(TestData.student1);
    }

    @Test
    public void givenExistentStudent_whenCreate_thenNotUniqueNameExceptionThrow() {
        Student student = new Student();
        student.setFirstName(TestData.student1.getFirstName());
        student.setLastName(TestData.student1.getLastName());
        when(studentDao.findByFirstNameAndLastName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(TestData.student1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.create(student));

        String expectedMessage = format("Student with name %s %s already exist",
            student.getFirstName(), student.getLastName());
        verify(studentDao, never()).save(student);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenCreate_thenNotAvailableGroupExceptionThrow() {
        when(universityProperties.getMaxGroupSize()).thenReturn(30);
        when(studentDao.findByFirstNameAndLastName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.empty());
        TestData.group1.setStudents(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.create(TestData.student1));

        String expectedMessage = "Group with name NG-12 not available. Max group size = 30 has already been reached";
        verify(studentDao, never()).save(TestData.student1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudentId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(studentDao.findById(1)).thenReturn(Optional.of(TestData.student1));

        assertEquals(TestData.student1, studentService.getById(1));
    }

    @Test
    public void givenNotExistentStudentId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(studentDao.findById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.getById(20));

        String expectedMessage = "Student with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentStudent_whenUpdate_thenUpdated() throws NotAvailableGroupException, NotUniqueNameException, EntityNotFoundException {
        when(universityProperties.getMaxGroupSize()).thenReturn(30);
        when(studentDao.findByFirstNameAndLastName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.of(TestData.student1));
        TestData.group1.setStudents(students);

        studentService.update(TestData.student1);

        verify(studentDao).save(TestData.student1);
    }

    @Test
    public void givenStudentWithOtherStudentName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        when(studentDao.findByFirstNameAndLastName(TestData.student2.getFirstName(), TestData.student2.getLastName())).thenReturn(Optional.of(TestData.student1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> studentService.update(TestData.student2));

        String expectedMessage = format("Student with name %s %s already exist",
            TestData.student2.getFirstName(), TestData.student2.getLastName());
        verify(studentDao, never()).save(TestData.student2);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenNotAvailableGroup_whenUpdate_thenNotAvailableGroupExceptionThrow() {
        when(universityProperties.getMaxGroupSize()).thenReturn(30);
        when(studentDao.findByFirstNameAndLastName(TestData.student1.getFirstName(), TestData.student1.getLastName())).thenReturn(Optional.of(TestData.student1));
        TestData.group1.setStudents(generateStudents());

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> studentService.update(TestData.student1));

        String expectedMessage = "Group with name NG-12 not available. Max group size = 30 has already been reached";
        verify(studentDao, never()).save(TestData.student1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        studentService.delete(1);

        verify(studentDao).deleteById(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Student> studentPage =
            new PageImpl<Student>(students, pageable, students.size());
        when(studentDao.findAll(pageable)).thenReturn(studentPage);

        assertEquals(studentPage, studentService.getAll(pageable));
    }


    private List<Student> generateStudents() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < universityProperties.getMaxGroupSize(); i++) {
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
