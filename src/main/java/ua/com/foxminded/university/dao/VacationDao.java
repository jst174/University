package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.Optional;

public interface VacationDao extends Dao<Vacation> {

    Optional<Vacation> getByTeacherAndLessonDate(Teacher teacher, LocalDate lessonDate);

    Optional<Vacation> getByTeacherAndVacationDates(Vacation vacation);
}
