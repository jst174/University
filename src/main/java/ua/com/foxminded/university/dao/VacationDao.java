package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.Optional;

public interface VacationDao extends Dao<Vacation> {

    Optional<Vacation> getByTeacherAndDate(Teacher teacher, LocalDate date);

    Optional<Vacation> getByTeacherAndVacationDates(Teacher teacher, LocalDate start, LocalDate end);
}
