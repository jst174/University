package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Course;

import java.util.List;

public interface CourseService extends Service <Course> {

    List<Course> getByTeacherId(int teacherId);
}
