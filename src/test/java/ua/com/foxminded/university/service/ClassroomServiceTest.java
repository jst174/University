package ua.com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.exceptions.ServiceException;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomDao classroomDao;
    @InjectMocks
    private ClassroomService classroomService;
    private List<Classroom> classrooms;

    @BeforeEach
    public void setUp() {
        classrooms = new ArrayList<>();
        Classroom classroom1 = new Classroom(101, 30);
        classroom1.setId(1);
        Classroom classroom2 = new Classroom(202, 60);
        classroom2.setId(2);
        classrooms.add(classroom1);
        classrooms.add(classroom2);
    }

    @Test
    public void givenNewClassroom_whenCreateClassroom_thenCreated() {
        Classroom classroom = new Classroom(102, 30);

        when(classroomDao.getById(classroom.getId())).thenReturn(Optional.empty());
        when(classroomDao.findByNumber(classroom.getNumber())).thenReturn(Optional.empty());

        classroomService.createClassroom(classroom);

        verify(classroomDao).create(classroom);
    }

    @Test
    public void givenClassroomWithExistentNumber_whenCreateClassroom_thenThrowException() {
        Classroom classroom = new Classroom(classrooms.get(0).getNumber(), 40);

        when(classroomDao.getById(classroom.getId())).thenReturn(Optional.of(classroom));
        when(classroomDao.findByNumber(classroom.getNumber())).thenReturn(Optional.of(classrooms.get(0)));

        Exception exception = assertThrows(ServiceException.class, () -> classroomService.createClassroom(classroom));

        String expectedMessage = format("Classroom with number %s already exist", classroom.getNumber());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    public void givenExistClassroomId_whenGetById_thenReturn() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getById(1)).thenReturn(Optional.of(classroom));

        assertEquals(classroom, classroomService.getById(1));
    }

    @Test
    public void givenNotExistentClassroomId_whenGetById_thenThrowException() {
        when(classroomDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceException.class, () -> classroomService.getById(20));

        String expectedMessage = format("Classroom with id %s not found", 20);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenExistentClassroom_whenUpdate_thenUpdated() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getById(classroom.getId())).thenReturn(Optional.of(classroom));
        when(classroomDao.findByNumber(classroom.getNumber())).thenReturn(Optional.of(classroom));

        classroomService.update(classroom);

        verify(classroomDao).update(classroom);
    }

    @Test
    public void givenClassroomWithOtherClassroomNumber_whenUpdate_thenThrowException() {
        Classroom classroom1 = classrooms.get(0);
        classroom1.setNumber(202);
        Classroom classroom2 = classrooms.get(1);

        when(classroomDao.getById(classroom1.getId())).thenReturn(Optional.of(classroom1));
        when(classroomDao.findByNumber(classroom1.getNumber())).thenReturn(Optional.of(classroom2));

        Exception exception = assertThrows(ServiceException.class, () -> classroomService.update(classroom1));

        String expectedMessage = format("Classroom with number %s already exist", classroom1.getNumber());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        Classroom classroom = classrooms.get(0);

        classroomService.delete(1);

        verify(classroomDao).delete(1);
    }

}
