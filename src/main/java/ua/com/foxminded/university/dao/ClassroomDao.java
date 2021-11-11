package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends Dao<Classroom>{

    Optional<Classroom> findByNumber(int number);
}
