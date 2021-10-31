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
        Classroom classroom = new Classroom(102, 30);

        when(classroomDao.findByNumber(102)).thenReturn(classroom);

        classroomService.createClassroom(classroom);

        verify(classroomDao).create(classroom);
    }


    @Test
    public void givenExistClassroomId_whenGetById_thenReturn() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getById(1)).thenReturn(classroom);

        assertEquals(classroom, classroomService.getById(1));
    }

    @Test
    public void givenExistentClassroom_whenUpdate_thenUpdated() {
        Classroom classroom = classrooms.get(0);

        when(classroomDao.getById(1)).thenReturn(classroom);

        classroomService.update(classroom);

        verify(classroomDao).update(classroom);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        Classroom classroom = classrooms.get(0);

        classroomService.delete(1);

        verify(classroomDao).delete(1);
    }
}
