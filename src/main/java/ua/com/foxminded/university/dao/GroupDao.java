package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupDao extends Dao<Group> {

    List<Group> getByLessonId(int lessonId);

    Optional<Group> getByName(String name);
}
