package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.config.UniversityConfigProperties;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableTimeException;
import ua.com.foxminded.university.exceptions.NotUniqueTimeException;
import ua.com.foxminded.university.model.Time;

import java.util.List;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class TimeService {

    private static final Logger logger = LoggerFactory.getLogger(TimeService.class);

    private TimeDao timeDao;
    private UniversityConfigProperties universityProperties;

    public TimeService(TimeDao timeDao, UniversityConfigProperties universityProperties) {
        this.timeDao = timeDao;
        this.universityProperties = universityProperties;
    }

    public void create(Time time) throws NotUniqueTimeException, NotAvailableTimeException {
        logger.debug("Creating time {} - {}", time.getStartTime(), time.getEndTime());
        verifyTimeUniqueness(time);
        verifyLessonDuration(time);
        verifyTimeCrossing(time);
        timeDao.save(time);
    }

    public Time getById(int id) throws EntityNotFoundException {
        logger.debug("Getting time with id = {}", id);
        return timeDao.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Time with id = %s not found", id)));
    }

    public void update(Time time) throws NotUniqueTimeException, NotAvailableTimeException {
        logger.debug("Updating time with id = {}", time.getId());
        verifyTimeUniqueness(time);
        verifyLessonDuration(time);
        verifyTimeCrossing(time);
        timeDao.save(time);
    }

    public void delete(int id) {
        logger.debug("Deleting time with id = {}", id);
        timeDao.deleteById(id);
    }

    public List<Time> getAll() {
        logger.debug("Getting all times");
        return timeDao.findAll();
    }

    private void verifyTimeUniqueness(Time time) throws NotUniqueTimeException {
        if (timeDao.findByTime(time.getStartTime(), time.getEndTime())
            .filter(t -> t.getId() != time.getId())
            .isPresent()) {
            throw new NotUniqueTimeException(format("Time with start = %s and end = %s already exist",
                time.getStartTime(), time.getEndTime()));
        }
    }

    private void verifyLessonDuration(Time time) throws NotAvailableTimeException {
        int minLessonDurationInMinutes = universityProperties.getMinLessonDurationInMinutes();
        if (MINUTES.between(time.getStartTime(), time.getEndTime())
            < minLessonDurationInMinutes) {
            throw new NotAvailableTimeException(format("Duration less than %s minute(s)",
                minLessonDurationInMinutes));
        }
    }

    private void verifyTimeCrossing(Time newTime) throws NotAvailableTimeException {
        List<Time> times = timeDao.findAll();
        if (times.stream().anyMatch(time ->
            ((newTime.getStartTime().isAfter(time.getStartTime()) && newTime.getStartTime().isBefore(time.getEndTime()))
                || (newTime.getStartTime().isBefore(time.getStartTime()) && (newTime.getEndTime().isBefore(time.getEndTime())
                || newTime.getEndTime().isAfter(time.getEndTime())))))) {
            throw new NotAvailableTimeException(format("Time with start = %s and end = %s crossing with other time",
                newTime.getStartTime(), newTime.getEndTime()));
        }
    }
}
