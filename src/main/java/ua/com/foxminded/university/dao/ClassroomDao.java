package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;

import java.util.List;

public interface ClassroomDao {

    void create(Classroom classroom);

    Classroom getById(int id);

    void update(int id, Classroom classroom);

    void delete(int id);

    List<Classroom> getAll();
}
