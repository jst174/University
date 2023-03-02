package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {

    Optional<Vacation> findByTeacherIdAndStartAndEnding(
        @Param("id") int id,
        @Param("start") LocalDate start,
        @Param("ending") LocalDate ending);

    @Query("SELECT v FROM Vacation v WHERE v.teacher.id=:id AND :date BETWEEN v.start AND v.ending")
    Optional<Vacation> findByTeacherAndDate(
        @Param("id") int id,
        @Param("date") LocalDate date
    );
}
