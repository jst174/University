package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, Integer> {

    Optional<Time> findByStartTimeAndEndTime(@Param("start") LocalTime start,
                                             @Param("end") LocalTime end);
}
