package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailablePeriodException;
import ua.com.foxminded.university.exceptions.NotUniqueVacationDatesException;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class VacationService {

    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    private VacationDao vacationDao;
    private TeacherService teacherService;
    @Value("#{${maxPeriodsVacation}}")
    private Map<AcademicDegree, Integer> maxPeriodsVacation;

    public VacationService(VacationDao vacationDao, TeacherService teacherService) {
        this.vacationDao = vacationDao;
        this.teacherService = teacherService;
    }

    public void create(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException, EntityNotFoundException {
        logger.debug("Creating vacation where start = {} and end = {}", vacation.getStart(), vacation.getEnd());
        verifyUniqueness(vacation);
        verifyPeriodAvailability(vacation);
        vacationDao.create(vacation);
    }

    public Vacation getById(int id) throws EntityNotFoundException {
        logger.debug("Getting vacation with id = {}", id);
        return vacationDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Vacation with id = %s not not found", id)));
    }

    public void update(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException, EntityNotFoundException {
        logger.debug("Updating vacation with id = {}", vacation.getId());
        verifyUniqueness(vacation);
        verifyPeriodAvailability(vacation);
        vacationDao.update(vacation);
    }

    public void delete(int id) {
        logger.debug("Deleting vacation with id = {}", id);
        vacationDao.delete(id);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        logger.debug("Getting teacher vacations with teacher id = {}", teacherId);
        return vacationDao.getByTeacherId(teacherId);
    }

    public Page<Vacation> getAll(Pageable pageable) {
        logger.debug("Getting all vacations");
        return vacationDao.getAll(pageable);
    }

    private void verifyUniqueness(Vacation vacation) throws NotUniqueVacationDatesException {
        if (vacationDao.getByTeacherAndVacationDates(vacation)
            .filter(v -> v.getId() != vacation.getId())
            .isPresent()) {
            throw new NotUniqueVacationDatesException(format("Teacher's vacation with start = %s and end = %s already exist",
                vacation.getStart(), vacation.getEnd()));
        }
    }

    private void verifyPeriodAvailability(Vacation vacation) throws NotAvailablePeriodException, EntityNotFoundException {
        List<Vacation> vacations = vacationDao.getByTeacherId(vacation.getTeacher().getId());
        vacations.add(vacation);
        Teacher teacher = teacherService.getById(vacation.getTeacher().getId());
        int maxVacationPeriod = maxPeriodsVacation.get(teacher.getAcademicDegree());
        int sumVacations = vacations.stream()
            .mapToInt(v -> (int) DAYS.between(v.getStart(), v.getEnd()))
            .sum();
        if (maxVacationPeriod < sumVacations) {
            throw new NotAvailablePeriodException(format("Vacation with period = %s days not available. " +
                    "The sum of all vacations = %s is longer than the maximum allowed = %s",
                DAYS.between(vacation.getStart(), vacation.getEnd()), sumVacations, maxVacationPeriod));
        }
    }
}
