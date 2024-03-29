package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import ua.com.foxminded.university.dao.ClassroomRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;
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
    public void givenNewClassroom_whenCreateClassroom_thenCreated() throws NotUniqueNameException {
        Classroom classroom = new Classroom(102, 30);
        when(classroomRepository.findByNumber(classroom.getNumber())).thenReturn(Optional.empty());

        classroomService.createClassroom(classroom);

        verify(classroomRepository).save(classroom);
    }

    @Test
    public void givenClassroomWithExistentNumber_whenCreateClassroom_thenNotUniqueNameExceptionThrow() {
        Classroom classroom = new Classroom(classrooms.get(0).getNumber(), 40);
        when(classroomRepository.findByNumber(classroom.getNumber())).thenReturn(Optional.of(classrooms.get(0)));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> classroomService.createClassroom(classroom));

        String expectedMessage = "Classroom with number = 101 already exist";
        verify(classroomRepository, never()).save(classroom);
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void givenExistClassroomId_whenGetById_thenReturn() throws EntityNotFoundException {
        Classroom classroom = classrooms.get(0);

        when(classroomRepository.findById(1)).thenReturn(Optional.of(classroom));

        assertEquals(classroom, classroomService.getById(1));
    }

    @Test
    public void givenNotExistentClassroomId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(classroomRepository.findById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> classroomService.getById(20));

        String expectedMessage = "Classroom with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentClassroom_whenUpdate_thenUpdated() throws NotUniqueNameException {
        Classroom classroom = classrooms.get(0);
        when(classroomRepository.findByNumber(classroom.getNumber())).thenReturn(Optional.of(classroom));

        classroomService.update(classroom);

        verify(classroomRepository).save(classroom);
    }

    @Test
    public void givenClassroomWithOtherClassroomNumber_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Classroom classroom1 = classrooms.get(0);
        classroom1.setNumber(202);
        Classroom classroom2 = classrooms.get(1);
        when(classroomRepository.findByNumber(classroom1.getNumber())).thenReturn(Optional.of(classroom2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> classroomService.update(classroom1));

        String expectedMessage = "Classroom with number = 202 already exist";
        verify(classroomRepository, never()).save(classroom1);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        classroomService.delete(1);

        verify(classroomRepository).deleteById(1);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Classroom> classroomPage =
            new PageImpl<Classroom>(classrooms, pageable, classrooms.size());
        when(classroomRepository.findAll(pageable)).thenReturn(classroomPage);

        assertEquals(classroomPage, classroomService.getAll(pageable));
    }

    @Test
    public void whenGetAll_thenReturn() {
        when(classroomRepository.findAll()).thenReturn(classrooms);

        assertEquals(classrooms, classroomService.getAll());
    }

}
