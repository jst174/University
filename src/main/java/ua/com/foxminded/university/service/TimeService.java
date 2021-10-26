package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;

import java.util.List;

@Service
public class TimeService {

    private TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public void create(Time time) {
        if (!timeIsExist(time)) {
            timeDao.create(time);
        } else {
            throw new IllegalArgumentException("time already exist");
        }

    }

    public Time getById(int id) {
        if (idIsExist(id)) {
            return timeDao.getById(id);
        } else {
            throw new IllegalArgumentException("time is not found");
        }

    }

    public void update(Time time) {
        if (idIsExist(time.getId())) {
            timeDao.update(time);
        } else {
            throw new IllegalArgumentException("time is not found");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            timeDao.delete(id);
        } else {
            throw new IllegalArgumentException("time is not found");
        }

    }

    public List<Time> getAll() {
        return timeDao.getAll();
    }

    private boolean timeIsExist(Time time) {
        List<Time> times = timeDao.getAll();
        return times.stream().anyMatch(time::equals);
    }

    private boolean idIsExist(int id) {
        List<Time> times = timeDao.getAll();
        return times.stream().anyMatch(time -> time.getId() == id);
    }
}
