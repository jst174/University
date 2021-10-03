package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;

public interface VacationDao {
    void create(Vacation vacation);

    Vacation getById(int id);

    void update(int id, Vacation vacation);

    void delete(int id);

    List<Vacation> getAll();
}
