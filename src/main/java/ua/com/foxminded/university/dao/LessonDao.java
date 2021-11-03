package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getByTeacherId(int teacherId);
    List<Lesson> getByClassroomId(int classroomId);
    List<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher);
    List<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom);
    List<Lesson> getByDateAndTime(LocalDate date, Time time);
}
