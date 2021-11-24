package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDao extends Dao<Course> {

    List<Course> getByTeacherId(int teacherId);

    Optional<Course> getByName(String name);
}
