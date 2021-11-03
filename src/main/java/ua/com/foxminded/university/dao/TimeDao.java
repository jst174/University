package ua.com.foxminded.university.dao;

import org.springframework.cglib.core.Local;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeDao extends Dao<Time> {

    Optional<Time> getByTime(LocalTime start, LocalTime end);

}
