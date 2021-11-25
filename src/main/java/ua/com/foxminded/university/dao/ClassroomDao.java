package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Classroom;
import java.util.Optional;

public interface ClassroomDao extends Dao<Classroom>{

    Optional<Classroom> findByNumber(int number);
}
