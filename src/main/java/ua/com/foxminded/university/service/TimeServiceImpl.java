package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.util.List;

@Service
public class TimeServiceImpl implements TimeService {

    private TimeDao timeDao;

    public TimeServiceImpl(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    @Override
    public void create(Time time) {
        timeDao.create(time);
    }

    @Override
    public Time getById(int id) {
        return timeDao.getById(id);
    }

    @Override
    public void update(Time time) {
        timeDao.update(time);
    }

    @Override
    public void delete(int id) {
        timeDao.delete(id);
    }

    @Override
    public List<Time> getAll() {
        return timeDao.getAll();
    }
}
