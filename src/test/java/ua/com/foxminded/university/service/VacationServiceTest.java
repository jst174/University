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
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        teacher.setAcademicDegree(AcademicDegree.DOCTORAL);
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
        teacher2.setAcademicDegree(AcademicDegree.MASTER);
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
    public void givenExistentId_whenGetById_thenReturn() {
        Vacation vacation = vacations.get(0);

        when(vacationDao.getById(1)).thenReturn(Optional.of(vacation));

        assertEquals(vacation, vacationService.getById(1));
    }

    @Test
    public void givenExistentVacation_whenUpdate_thenUpdated() {
        Vacation vacation = vacations.get(0);

        when(vacationDao.getById(vacation.getId())).thenReturn(Optional.of(vacation));

        vacationService.update(vacation);

        verify(vacationDao).update(vacation);
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        vacationService.delete(1);

        verify(vacationDao).delete(1);
    }

    @Test
    public void givenExistentTeacherId_whenGetByTeacherId_whenReturn() {
        when(vacationDao.getByTeacherId(1)).thenReturn(vacations);

        assertEquals(vacations, vacationService.getByTeacherId(1));
    }
}
