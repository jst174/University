package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService {

    private HolidayDao holidayDao;

    public HolidayServiceImpl(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    @Override
    public void create(Holiday holiday) {
        holidayDao.create(holiday);
    }

    @Override
    public Holiday getById(int id) {
        return holidayDao.getById(id);
    }

    @Override
    public void update(Holiday holiday) {
        holidayDao.update(holiday);
    }

    @Override
    public void delete(int id) {
        holidayDao.delete(id);
    }

    @Override
    public List<Holiday> getAll() {
        return holidayDao.getAll();
    }
}
