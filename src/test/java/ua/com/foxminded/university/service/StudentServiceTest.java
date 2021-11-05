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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;
    private List<Student> students;
    private DataSource dataSource;
    private Group group;
    @Value("${max.group.size}")
    private int maxGroupSize;

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
    public void givenNewStudent_whenCreate_thenCreated() throws IOException {
        Student student = dataSource.generateStudent();
        student.setGroup(group);

        when(studentDao.getByGroupId(1)).thenReturn(students);
        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.empty());

        studentService.create(student);

        verify(studentDao).create(student);
    }

    @Test
    public void givenExistentStudent_whenCreate_thenNotCreated() {
        Student student = students.get(0);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));

        studentService.create(student);

        verify(studentDao, never()).create(student);
    }

    @Test
    public void givenNotAvailableGroup_whenCreate_thenNotCreated() throws IOException {
        Student student = dataSource.generateStudent();
        student.setGroup(group);

        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());
        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.empty());

        studentService.create(student);

        verify(studentDao, never()).create(student);
    }

    @Test
    public void givenExistentStudentId_whenGetById_thenReturn() {
        Student student = students.get(0);

        when(studentDao.getById(1)).thenReturn(Optional.of(student));

        assertEquals(student, studentService.getById(1));
    }

    @Test
    public void givenExistentStudent_whenUpdate_thenUpdated() {
        Student student = students.get(0);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    public void givenStudentWithOtherStudentName_whenUpdate_thenNotUpdated() {
        Student student1 = students.get(0);
        Student student2 = students.get(1);
        student1.setFirstName(student2.getFirstName());
        student1.setLastName(student2.getLastName());

        when(studentDao.getByName(student1.getFirstName(), student1.getLastName())).thenReturn(Optional.of(student2));

        studentService.update(student1);

        verify(studentDao, never()).update(student1);
    }

    @Test
    public void givenNotAvailableGroup_whenUpdate_thenNotUpdated() throws IOException {
        Student student = students.get(0);

        when(studentDao.getByName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));
        when(studentDao.getByGroupId(1)).thenReturn(generateStudents());

        studentService.update(student);

        verify(studentDao, never()).update(student);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        studentService.delete(1);

        verify(studentDao).delete(1);
    }

    private List<Student> generateStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < maxGroupSize; i++) {
            students.add(dataSource.generateStudent());
        }
        return students;
    }
}
