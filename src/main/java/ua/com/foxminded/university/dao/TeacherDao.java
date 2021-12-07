package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Teacher;

import java.util.Optional;

public interface TeacherDao extends Dao<Teacher> {

    Optional<Teacher> getByName(String firstName, String lastName);
}
