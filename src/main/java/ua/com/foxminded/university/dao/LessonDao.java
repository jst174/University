package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;

public interface LessonDao {

    void create(Lesson lesson);

    Lesson getById(int id);

    void update(int id, Lesson lesson);

    void delete(int id);

    List<Lesson> getAll();
}
