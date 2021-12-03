package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getByTeacherId(int teacherId);

    List<Lesson> getByClassroomId(int classroomId);

    Optional<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher);

    Optional<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom);

    List<Lesson> getByDateAndTime(LocalDate date, Time time);

    Page<Lesson> getAll(Pageable pageable);
}
