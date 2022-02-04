package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao extends Dao<Course> {

    Optional<Course> getByName(String name);
}
