package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;
import ua.com.foxminded.university.model.Vacation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;
    @Mock
    private TeacherDao teacherDao;
    @InjectMocks
    private VacationService vacationService;
    private List<Vacation> vacations;
    private List<Teacher> teachers;
    private DataSource dataSource;
    private Teacher teacher;

    @BeforeEach
    public void setUp() throws IOException {
        dataSource = new DataSource();
        vacations = new ArrayList<>();
        teacher = dataSource.generateTeacher();
        teacher.setId(1);
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 11, 5),
            LocalDate.of(2021, 11, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 5),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(1);
        vacations.add(vacation1);
        vacations.add(vacation2);

        teachers = new ArrayList<>();
        Teacher teacher2 = dataSource.generateTeacher();
        teacher2.setId(2);
        teachers.add(teacher);
        teachers.add(teacher2);
    }

    @Test
    public void givenNewVacation_whenCreate_thenCreated() throws IOException {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 3, 10),
            LocalDate.of(2021, 3, 20),
            teacher);

        when(vacationDao.getAll()).thenReturn(vacations);

        vacationService.create(vacation);

        verify(vacationDao).create(vacation);
    }

    @Test
    public void givenExistentVacation_whenCreate_thenThrowException() {
        Vacation vacation = vacations.get(0);

        when(vacationDao.getAll()).thenReturn(vacations);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> vacationService.create(vacation));

        assertEquals("vacation already exist", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() {
        Vacation vacation = vacations.get(0);

        when(vacationDao.getAll()).thenReturn(vacations);
        when(vacationDao.getById(1)).thenReturn(vacation);

        assertEquals(vacation, vacationService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenThrowException() {
        when(vacationDao.getAll()).thenReturn(vacations);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> vacationService.getById(3));

        assertEquals("vacation is not found", exception.getMessage());
    }

    @Test
    public void givenExistentVacation_whenUpdate_thenUpdated() {
        Vacation vacation = vacations.get(0);

        when(vacationDao.getAll()).thenReturn(vacations);

        vacationService.update(vacation);

        verify(vacationDao).update(vacation);
    }

    @Test
    public void givenNotExistentVacation_whenUpdate_thenThrowException() throws IOException {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 3, 10),
            LocalDate.of(2021, 3, 20),
            teacher);

        when(vacationDao.getAll()).thenReturn(vacations);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> vacationService.update(vacation));

        assertEquals("vacation is not found", exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        when(vacationDao.getAll()).thenReturn(vacations);

        vacationService.delete(1);

        verify(vacationDao).delete(1);
    }

    @Test
    public void givenNotExistentId_whenDeleted_thenThrowException() {
        when(vacationDao.getAll()).thenReturn(vacations);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> vacationService.delete(3));

        assertEquals("vacation is not found", exception.getMessage());
    }

    @Test
    public void givenExistentTeacherId_whenGetByTeacherId_whenReturn() {
        when(teacherDao.getAll()).thenReturn(teachers);
        when(vacationDao.getByTeacherId(1)).thenReturn(vacations);

        assertEquals(vacations, vacationService.getByTeacherId(1));
    }

    @Test
    public void givenNotExistentTeacherId_whenGetByTeacherId_thenThrowException() {
        when(teacherDao.getAll()).thenReturn(teachers);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> vacationService.getByTeacherId(3));

        assertEquals("teacher is not found", exception.getMessage());
    }
}
