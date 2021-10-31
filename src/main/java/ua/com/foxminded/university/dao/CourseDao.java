package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;

import java.util.List;

public interface CourseDao extends Dao<Course> {

    List<Course> getByTeacherId(int teacherId);

    Course getByName(String name);
}
