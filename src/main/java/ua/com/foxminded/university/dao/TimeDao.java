package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeDao extends Dao<Time> {

    Optional<Time> getByTime(LocalTime start, LocalTime end);

}
