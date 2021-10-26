package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;

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
        Classroom newClassroom = new Classroom(102, 30);

        when(classroomDao.getAll()).thenReturn(classrooms);

        classroomService.createClassroom(newClassroom);

        verify(classroomDao).create(newClassroom);
    }

    @Test
    public void givenExistClassroom_whenCreateClassroom_thenThrowException() {
        Classroom classroom = classrooms.get(0);
        when(classroomDao.getAll()).thenReturn(classrooms);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> classroomService.createClassroom(classroom));

        assertEquals("classroom with this number already exists", exception.getMessage());
    }

    @Test
    public void givenExistClassroomId_whenGetById_thenReturn() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getAll()).thenReturn(classrooms);
        when(classroomDao.getById(1)).thenReturn(classroom);

        assertEquals(classroom, classroomService.getById(1));
    }

    @Test
    public void givenNotExistentClassroomId_whenGetById_thenThrowException() {
        when(classroomDao.getAll()).thenReturn(classrooms);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> classroomService.getById(3));

        assertEquals("classroom is not found", exception.getMessage());
    }

    @Test
    public void givenExistentClassroom_whenUpdate_thenUpdated() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getAll()).thenReturn(classrooms);

        classroomService.update(classroom);

        verify(classroomDao).update(classroom);
    }

    @Test
    public void givenNotExistentClassroom_whenUpdate_thenThrowException() {
        Classroom classroom = new Classroom(102, 30);

        when(classroomDao.getAll()).thenReturn(classrooms);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> classroomService.update(classroom));

        assertEquals("classroom is not found", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getAll()).thenReturn(classrooms);

        classroomService.delete(1);

        verify(classroomDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDelete_thenThrowException() {
        when(classroomDao.getAll()).thenReturn(classrooms);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> classroomService.delete(3));

        assertEquals("classroom is not found", exception.getMessage());
    }
}
