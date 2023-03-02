package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query("SELECT l FROM Lesson l inner join l.groups g " +
        "WHERE g.id=:groupId AND l.date BETWEEN :fromDate AND :toDate")
    List<Lesson> findByGroupIdBetweenDates(
        @Param("groupId") int groupId,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate
    );

    @Query("SELECT l FROM Lesson l WHERE l.teacher.id=:teacherId " +
        "AND l.date BETWEEN :fromDate AND :toDate")
    List<Lesson> findByTeacherIdBetweenDates(
        @Param("teacherId") int teacherId,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate
    );

    Optional<Lesson> findByDateAndTimeIdAndTeacherId(
        @Param("date") LocalDate date,
        @Param("timeId") int timeId,
        @Param("teacherId") int teacherId
    );

    Optional<Lesson> findByDateAndTimeIdAndClassroomId(
        @Param("date") LocalDate date,
        @Param("timeId") int timeId,
        @Param("classroomId") int classroomId
    );

    @Query("SELECT l FROM Lesson l inner join l.groups g WHERE g.id=:groupId AND l.date=:date AND l.time=:time")
    List<Lesson> findByDateAndTimeAndGroupId(
        @Param("date") LocalDate date,
        @Param("time") Time time,
        @Param("groupId") int groupId
    );
}

