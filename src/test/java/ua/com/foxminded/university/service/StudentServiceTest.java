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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@ContextConfiguration(classes = {AppConfig.class})
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;
    private List<Student> students;
    private DataSource dataSource;
    private Group group;
    @Value("${group.capacity}")
    private int groupCapacity;

    @BeforeEach
    public void setUp() throws IOException {
        ReflectionTestUtils.setField(studentService, "groupCapacity", groupCapacity);
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

        studentService.create(student);

        verify(studentDao).create(student);
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

        when(studentDao.getById(1)).thenReturn(Optional.of(student));
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        studentService.delete(1);

        verify(studentDao).delete(1);
    }
}
