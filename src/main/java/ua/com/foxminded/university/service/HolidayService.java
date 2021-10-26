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
        if (!holidayIsExist(holiday)) {
            holidayDao.create(holiday);
        } else {
            throw new IllegalArgumentException("holiday already exist");
        }

    }

    public Holiday getById(int id) {
        if (idIsExist(id)) {
            return holidayDao.getById(id);
        } else {
            throw new IllegalArgumentException("holiday is not found");
        }
    }

    public void update(Holiday holiday) {
        if (idIsExist(holiday.getId())) {
            holidayDao.update(holiday);
        } else {
            throw new IllegalArgumentException("holiday is not found");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            holidayDao.delete(id);
        } else {
            throw new IllegalArgumentException("holiday is not found");
        }
    }

    public List<Holiday> getAll() {
        return holidayDao.getAll();
    }

    private boolean holidayIsExist(Holiday holiday) {
        List<Holiday> holidays = holidayDao.getAll();
        return holidays.stream().anyMatch(holiday::equals);
    }

    private boolean idIsExist(int id) {
        List<Holiday> holidays = holidayDao.getAll();
        return holidays.stream().anyMatch(holiday -> holiday.getId() == id);
    }
}
