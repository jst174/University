package ua.com.foxminded.university.service;

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
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailablePeriodException;
import ua.com.foxminded.university.exceptions.NotUniqueVacationDatesException;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Gender;
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
    private TeacherService teacherService;
    @InjectMocks
    private VacationService vacationService;
    private Map<AcademicDegree, Integer> maxPeriodsVacation;
    private List<Vacation> vacations;


    @BeforeEach
    public void setUp() throws IOException {
        maxPeriodsVacation = new HashMap<>();
        maxPeriodsVacation.put(AcademicDegree.ASSOCIATE, 15);
        maxPeriodsVacation.put(AcademicDegree.BACHELOR, 20);
        maxPeriodsVacation.put(AcademicDegree.MASTER, 25);
        maxPeriodsVacation.put(AcademicDegree.DOCTORAL, 30);
        ReflectionTestUtils.setField(vacationService, "maxPeriodsVacation", maxPeriodsVacation);
        vacations = new ArrayList<>(Arrays.asList(TestData.vacation1, TestData.vacation2));
    }

    @Test
    public void givenNewVacation_whenCreate_thenCreated() throws NotAvailablePeriodException, NotUniqueVacationDatesException, EntityNotFoundException {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 3, 1),
            LocalDate.of(2021, 3, 10),
            TestData.teacher1);
        TestData.teacher1.setVacations(vacations);
        when(teacherService.getById(vacation.getTeacher().getId())).thenReturn(TestData.teacher1);
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.empty());

        vacationService.create(vacation);

        verify(vacationDao).create(vacation);
    }

    @Test
    public void givenNewVacationWithNotAcceptablePeriod_whenCreate_thenNotAvailablePeriodExceptionThrow() throws EntityNotFoundException {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 3, 1),
            LocalDate.of(2021, 4, 30),
            TestData.teacher1);
        when(teacherService.getById(vacation.getTeacher().getId())).thenReturn(TestData.teacher1);
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.empty());
        TestData.teacher1.setVacations(vacations);

        Exception exception = assertThrows(NotAvailablePeriodException.class, () -> vacationService.create(vacation));

        String expectedMessage = "Vacation with period = 60 days not available. " +
            "The sum of all vacations = 70 is longer than the maximum allowed = 30";
        verify(vacationDao, never()).create(vacation);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentVacation_whenCreate_thenNotUniqueVacationDatesExceptionThrow() throws EntityNotFoundException {
        Vacation vacation = new Vacation();
        vacation.setStart(TestData.vacation1.getStart());
        vacation.setEnd(TestData.vacation1.getEnd());
        vacation.setTeacher(TestData.vacation1.getTeacher());
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.of(TestData.vacation1));

        Exception exception = assertThrows(NotUniqueVacationDatesException.class, () -> vacationService.create(vacation));

        String expectedMessage = "Teacher's vacation with start = 2021-11-05 and end = 2021-11-10 already exist";
        verify(vacationDao, never()).create(vacation);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(vacationDao.getById(1)).thenReturn(Optional.of(TestData.vacation1));

        assertEquals(TestData.vacation1, vacationService.getById(1));
    }

    @Test
    public void givenNotExistentId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(vacationDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> vacationService.getById(20));

        String expectedMessage = "Vacation with id = 20 not not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentVacation_whenUpdate_thenUpdated() throws NotAvailablePeriodException, NotUniqueVacationDatesException, EntityNotFoundException {
        when(vacationDao.getByTeacherAndVacationDates(TestData.vacation1)).thenReturn(Optional.of(TestData.vacation1));
        when(teacherService.getById(TestData.vacation1.getTeacher().getId())).thenReturn(TestData.teacher1);
        TestData.teacher1.setVacations(vacations);

        vacationService.update(TestData.vacation1);

        verify(vacationDao).update(TestData.vacation1);
    }

    @Test
    public void givenVacationWithOtherVacationTeacherAndVacationDates_whenUpdate_thenNotUniqueVacationDatesExceptionThrow() {
        when(vacationDao.getByTeacherAndVacationDates(TestData.vacation2)).thenReturn(Optional.of(TestData.vacation1));

        Exception exception = assertThrows(NotUniqueVacationDatesException.class, () -> vacationService.update(TestData.vacation2));

        String expectedMessage = "Teacher's vacation with start = 2021-05-05 and end = 2021-05-10 already exist";
        verify(vacationDao, never()).update(TestData.vacation2);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenVacationWithNotAcceptablePeriod_whenUpdate_thenNotAvailablePeriodExceptionThrow() throws EntityNotFoundException {
        Vacation vacation = new Vacation();
        vacation.setStart(LocalDate.of(2021, 1, 1));
        vacation.setEnd(LocalDate.of(2021, 1, 26));
        vacation.setTeacher(TestData.teacher1);
        TestData.teacher1.setVacations(vacations);
        when(vacationDao.getByTeacherAndVacationDates(vacation)).thenReturn(Optional.of(vacation));
        when(teacherService.getById(vacation.getTeacher().getId())).thenReturn(TestData.teacher1);

        Exception exception = assertThrows(NotAvailablePeriodException.class, () -> vacationService.update(vacation));

        String expectedMessage = "Vacation with period = 25 days not available. " +
            "The sum of all vacations = 35 is longer than the maximum allowed = 30";
        verify(vacationDao, never()).update(vacation);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentId_whenDelete_thenDeleted() {
        vacationService.delete(1);

        verify(vacationDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Vacation> vacationPage =
            new PageImpl<Vacation>(vacations, pageable, vacations.size());
        when(vacationDao.getAll(pageable)).thenReturn(vacationPage);

        assertEquals(vacationPage, vacationService.getAll(pageable));
    }

    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setAcademicDegree(AcademicDegree.DOCTORAL)
            .setId(1)
            .build();
        Vacation vacation1 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 11, 5))
            .setEnd(LocalDate.of(2021, 11, 10))
            .setTeacher(teacher1)
            .setId(1)
            .build();
        Vacation vacation2 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 5, 5))
            .setEnd(LocalDate.of(2021, 5, 10))
            .setTeacher(teacher1)
            .setId(2)
            .build();
    }
}
