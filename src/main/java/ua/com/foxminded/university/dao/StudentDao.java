package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao extends Dao<Student> {

    Optional<Student> getByName(String firstName, String lastName);
}
