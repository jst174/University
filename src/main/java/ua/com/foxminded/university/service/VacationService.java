package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailablePeriodException;
import ua.com.foxminded.university.exceptions.NotUniqueVacationDatesException;
import ua.com.foxminded.university.model.AcademicDegree;
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
    @Value("#{${maxPeriodsVacation}}")
    private Map<AcademicDegree, Integer> maxPeriodsVacation;

    public VacationService(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    public void create(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException {
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

    public void update(Vacation vacation) throws NotUniqueVacationDatesException, NotAvailablePeriodException {
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

    public Page<Vacation> findPaginated(Pageable pageable) {
        List<Vacation> vacations = vacationDao.getAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Vacation> list;
        if (vacations.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, vacations.size());
            list = vacations.subList(startItem, toIndex);
        }
        return new PageImpl<Vacation>(list, PageRequest.of(currentPage, pageSize), vacations.size());
    }

    private void verifyUniqueness(Vacation vacation) throws NotUniqueVacationDatesException {
        if (vacationDao.getByTeacherAndVacationDates(vacation)
            .filter(v -> v.getId() != vacation.getId())
            .isPresent()) {
            throw new NotUniqueVacationDatesException(format("Teacher's vacation with start = %s and end = %s already exist",
                vacation.getStart(), vacation.getEnd()));
        }
    }

    private void verifyPeriodAvailability(Vacation vacation) throws NotAvailablePeriodException {
        List<Vacation> vacations = vacationDao.getByTeacherId(vacation.getTeacher().getId());
        vacations.add(vacation);
        int maxVacationPeriod = maxPeriodsVacation.get(vacation.getTeacher().getAcademicDegree());
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
