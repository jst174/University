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
import java.util.*;

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
    private Map<AcademicDegree, Integer> maxPeriodsVacation;


    @BeforeEach
    public void setUp() throws IOException {
        maxPeriodsVacation = new HashMap<>();
        maxPeriodsVacation.put(AcademicDegree.ASSOCIATE, 15);
        maxPeriodsVacation.put(AcademicDegree.BACHELOR, 20);
        maxPeriodsVacation.put(AcademicDegree.MASTER, 25);
        maxPeriodsVacation.put(AcademicDegree.DOCTORAL, 30);

        ReflectionTestUtils.setField(vacationService, "maxPeriodsVacation", maxPeriodsVacation);
        dataSource = new DataSource();
        vacations = new ArrayList<>();
        teacher = dataSource.generateTeacher();
        teacher.setAcademicDegree(AcademicDegree.DOCTORAL);
        teacher.setId(1);
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 11, 5),
            LocalDate.of(2021, 11, 10),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 5),
            LocalDate.of(2021, 5, 10),
            teacher
        );
        vacation2.setId(2);
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
            LocalDate.of(2021, 3, 1),
            LocalDate.of(2021, 3, 10),
            teacher);


        when(vacationDao.getById(vacation.getId())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherId(vacation.getTeacher().getId())).thenReturn(vacations);

        vacationService.create(vacation);

        verify(vacationDao).create(vacation);
    }

    @Test
    public void givenNewVacationWithNotAcceptablePeriod_whenCreate_thenNotCreated(){
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 3, 1),
            LocalDate.of(2021, 4, 30),
            teacher);

        when(vacationDao.getById(vacation.getId())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherId(vacation.getTeacher().getId())).thenReturn(vacations);

        vacationService.create(vacation);

        verify(vacationDao, never()).create(vacation);
    }

    @Test
    public void givenExistentVacation_whenCreate_thenNotCreated(){
        Vacation vacation = vacations.get(0);

        when(vacationDao.getById(vacation.getId())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.of(vacation));

        vacationService.create(vacation);

        verify(vacationDao, never()).create(vacation);
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
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.of(vacation));
        when(vacationDao.getByTeacherId(vacation.getTeacher().getId())).thenReturn(vacations);

        vacationService.update(vacation);

        verify(vacationDao).update(vacation);
    }

    @Test
    public void givenVacationWithOtherVacationTeacherAndVacationDates_whenUpdate_thenNotUpdated(){
        Vacation vacation1 = vacations.get(0);
        Vacation vacation2 = vacations.get(1);
        vacation1.setTeacher(vacation2.getTeacher());
        vacation1.setStart(vacation2.getStart());
        vacation1.setEnd(vacation2.getEnd());

        when(vacationDao.getById(vacation1.getId())).thenReturn(Optional.of(vacation1));
        when(vacationDao.getByTeacherAndVacationDates(vacation1)).thenReturn(Optional.of(vacation2));

        vacationService.update(vacation1);

        verify(vacationDao, never()).update(vacation1);
    }

    @Test
    public void givenVacationWithNotAcceptablePeriod_whenUpdate_thenNotUpdated(){
        Vacation vacation = vacations.get(0);
        vacation.setStart(LocalDate.of(2021,1,1));
        vacation.setEnd(LocalDate.of(2021,1,26));

        when(vacationDao.getById(vacation.getId())).thenReturn(Optional.of(vacation));
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.of(vacation));
        when(vacationDao.getByTeacherId(vacation.getTeacher().getId())).thenReturn(vacations);

        vacationService.update(vacation);

        verify(vacationDao, never()).update(vacation);
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
