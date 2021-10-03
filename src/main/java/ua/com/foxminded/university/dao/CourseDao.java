package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;

import java.util.List;

public interface CourseDao {

    void create(Course course);

    Course getById(int id);

    void update(int id, Course course);

    void delete(int id);

    List<Course> getAll();
}
