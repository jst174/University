package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonDao extends Dao<Lesson> {

    Optional<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher);

    Optional<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom);

    List<Lesson> getByGroupIdBetweenDates(int groupId, LocalDate fromDate, LocalDate toDate);

    List<Lesson> getByTeacherIdBetweenDates(int teacherId, LocalDate fromDate, LocalDate toDate);

    List<Lesson> getByDateAndTimeAndGroupId(LocalDate date, Time time, int groupId);
}
