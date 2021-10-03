package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Time;

import java.util.List;

public interface TimeDao {
    void create(Time time);

    Time getById(int id);

    void update(int id, Time time);

    void delete(int id);

    List<Time> getAll();
}
