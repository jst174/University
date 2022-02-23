package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeDao extends JpaRepository<Time, Integer> {

    @Query("SELECT t FROM Time t WHERE t.startTime = :start AND t.endTime = :end")
    Optional<Time> findByTime(@Param("start") LocalTime start,
                              @Param("end") LocalTime end);
}
