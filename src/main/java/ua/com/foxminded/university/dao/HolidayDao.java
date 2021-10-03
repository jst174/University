package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Holiday;

import java.util.List;

public interface HolidayDao {

    void create(Holiday holiday);

    Holiday getById(int id);

    void update(int id, Holiday holiday);

    void delete(int id);

    List<Holiday> getAll();
}
