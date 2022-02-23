package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailablePeriodException;
import ua.com.foxminded.university.exceptions.NotUniqueVacationDatesException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.config.UniversityConfigProperties;

import java.util.List;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class VacationService {

    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    private VacationDao vacationDao;
    private TeacherService teacherService;
    private UniversityConfigProperties universityProperties;

    public VacationService(VacationDao vacationDao, TeacherService teacherService, UniversityConfigProperties universityProperties) {
        this.vacationDao = vacationDao;
        this.teacherService = teacherService;
        this.universityProperties = universityProperties;
    }

    public void create(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException, EntityNotFoundException {
        logger.debug("Creating vacation where start = {} and end = {}", vacation.getStart(), vacation.getEnding());
        verifyUniqueness(vacation);
        verifyPeriodAvailability(vacation);
        vacationDao.save(vacation);
    }

    public Vacation getById(int id) throws EntityNotFoundException {
        logger.debug("Getting vacation with id = {}", id);
        return vacationDao.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Vacation with id = %s not not found", id)));
    }

    public void update(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException, EntityNotFoundException {
        logger.debug("Updating vacation with id = {}", vacation.getId());
        verifyUniqueness(vacation);
        verifyPeriodAvailability(vacation);
        vacationDao.save(vacation);
    }

    public void delete(int id) {
        logger.debug("Deleting vacation with id = {}", id);
        vacationDao.deleteById(id);
    }

    public Page<Vacation> getAll(Pageable pageable) {
        logger.debug("Getting all vacations");
        return vacationDao.findAll(pageable);
    }

    private void verifyUniqueness(Vacation vacation) throws NotUniqueVacationDatesException {
        if (vacationDao.findByTeacherAndVacationDates(vacation.getTeacher().getId(), vacation.getStart(), vacation.getEnding())
            .filter(v -> v.getId() != vacation.getId())
            .isPresent()) {
            throw new NotUniqueVacationDatesException(format("Teacher's vacation with start = %s and end = %s already exist",
                vacation.getStart(), vacation.getEnding()));
        }
    }

    private void verifyPeriodAvailability(Vacation vacation) throws NotAvailablePeriodException, EntityNotFoundException {
        List<Vacation> vacations = vacation.getTeacher().getVacations();
        vacations.add(vacation);
        Teacher teacher = teacherService.getById(vacation.getTeacher().getId());
        int maxVacationPeriod = universityProperties.getMaxPeriodsVacation().get(teacher.getAcademicDegree());
        int sumVacations = vacations.stream()
            .mapToInt(v -> (int) DAYS.between(v.getStart(), v.getEnding()))
            .sum();
        if (maxVacationPeriod < sumVacations) {
            throw new NotAvailablePeriodException(format("Vacation with period = %s days not available. " +
                    "The sum of all vacations = %s is longer than the maximum allowed = %s",
                DAYS.between(vacation.getStart(), vacation.getEnding()), sumVacations, maxVacationPeriod));
        }
    }
}
