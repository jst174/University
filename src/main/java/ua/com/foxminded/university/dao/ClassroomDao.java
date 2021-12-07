package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Classroom;

import java.util.Optional;

public interface ClassroomDao extends Dao<Classroom> {

    Optional<Classroom> findByNumber(int number);
}
