package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.time.Duration;
import java.util.List;

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
        if ((!isUnique(time)) && (isNotLess30Minutes(time)) && (isTimeNotCrosses(time))) {
            timeDao.create(time);
        }
    }

    public Time getById(int id) {
        return timeDao.getById(id);
    }

    public void update(Time time) {
        if ((timeDao.getById(time.getId()).equals(time)) && (isNotLess30Minutes(time)) && (isTimeNotCrosses(time))) {
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
        return !timeDao.getByTime(time.getStartTime(), time.getEndTime()).equals(time);
    }

    private boolean isNotLess30Minutes(Time time) {
        return Duration.between(time.getStartTime(), time.getEndTime()).getSeconds()
            >= Duration.ofMinutes(minLessonDuration).getSeconds();
    }

    private boolean isTimeNotCrosses(Time newTime) {
        List<Time> times = timeDao.getAll();
        return times.stream().allMatch(time -> (((newTime.getStartTime().isBefore(time.getStartTime()))
            && (newTime.getEndTime().isBefore(time.getStartTime())))
            || ((newTime.getStartTime().isAfter(time.getEndTime())))));
    }
}
