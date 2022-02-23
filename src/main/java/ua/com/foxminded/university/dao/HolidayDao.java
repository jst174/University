package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.Optional;

public interface HolidayDao extends JpaRepository<Holiday, Integer> {

    @Query("SELECT h FROM Holiday h WHERE h.date = :date")
    Optional<Holiday> findByDate(@Param("date") LocalDate date);
}
