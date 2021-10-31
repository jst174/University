package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.util.List;

@Service
public class HolidayService {

    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) {
        if (!isUnique(holiday)) {
            holidayDao.create(holiday);
        }
    }

    public Holiday getById(int id) {
        return holidayDao.getById(id);
    }

    public void update(Holiday holiday) {
        if (holidayDao.getById(holiday.getId()).equals(holiday)) {
            holidayDao.update(holiday);
        }
    }

    public void delete(int id) {
        holidayDao.delete(id);
    }

    public List<Holiday> getAll() {
        return holidayDao.getAll();
    }

    private boolean isUnique(Holiday holiday) {
        return !holidayDao.getByDate(holiday.getDate()).equals(holiday);
    }
}
