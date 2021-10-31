package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Time;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getByTeacherId(int teacherId);
    List<Lesson> getByClassroomId(int classroomId);
    List<Lesson> getByDateAndTime(LocalDate date, Time time);
}
