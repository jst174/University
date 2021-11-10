package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@PropertySource("classpath:application.properties")
@Service
public class TimeService {

    private TimeDao timeDao;
    @Value("${lesson.min.duration}")
    private int minLessonDuration;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public void create(Time time) {
        if ((isUnique(time)) && (isNotLessMinLessonDuration(time)) && (isTimeNotCrosses(time))) {
            timeDao.create(time);
        }
    }

    public Time getById(int id) {
        return timeDao.getById(id).orElseThrow();
    }

    public void update(Time time) {
        if ((isUnique(time)) && (isNotLessMinLessonDuration(time))
            && (isTimeNotCrosses(time))) {
            timeDao.update(time);
        }
    }

    public void delete(int id) {
        timeDao.delete(id);
    }

    public List<Time> getAll() {
        return timeDao.getAll();
    }

    private boolean isUnique(Time time) {
        if (timeDao.getById(time.getId()).isEmpty()) {
            return timeDao.getByTime(time.getStartTime(), time.getEndTime()).isEmpty();
        } else {
            return timeDao.getByTime(time.getStartTime(), time.getEndTime()).get()
                .getId() == time.getId();
        }
    }

    private boolean isNotLessMinLessonDuration(Time time) {
        return MINUTES.between(time.getStartTime(), time.getEndTime())
            >= minLessonDuration;
    }

    private boolean isTimeNotCrosses(Time newTime) {
        List<Time> times = timeDao.getAll();
        return times.stream().allMatch(time -> (((newTime.getStartTime().isBefore(time.getStartTime()))
            && (newTime.getEndTime().isBefore(time.getStartTime())))
            || ((newTime.getStartTime().isAfter(time.getEndTime())))));
    }
}
