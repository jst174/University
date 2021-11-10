package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.Time;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcLessonDao implements LessonDao {

    private static final String SQL_INSERT_LESSON =
        "INSERT INTO lessons (classroom_id, course_id, teacher_id, date, time_id)" +
            "VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_LESSON = "SELECT * FROM lessons WHERE id = ?";
    private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET classroom_id = ?," +
        "course_id = ?, teacher_id = ?, date = ?, time_id = ? WHERE id = ?";
    private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM lessons";
    private static final String SQL_ADD_GROUP = "INSERT INTO lessons_groups(lesson_id, group_id) VALUES (?,?)";
    private static final String SQL_DELETE_GROUP = "DELETE FROM lessons_groups WHERE lesson_id = ? and group_id = ?";
    private static final String SQL_FIND_LESSONS_BY_TEACHER = "SELECT * FROM lessons WHERE teacher_id = ?";
    private static final String SQL_FIND_LESSONS_BY_CLASSROOM = "SELECT * FROM lessons WHERE classroom_id = ?";
    private static final String SQL_FIND_BY_DATE_AND_TIME_AND_TEACHER = "SELECT * FROM lessons WHERE date = ? and time_id = ? and teacher_id = ?";
    private static final String SQL_FIND_BY_DATE_AND_TIME_AND_CLASSROOM = "SELECT * FROM lessons WHERE date = ? and time_id = ? and classroom_id = ?";
    private static final String SQL_FIND_BY_DATE_AND_TIME = "SELECT * FROM lessons WHERE date = ? and time_id = ?";

    private JdbcTemplate jdbcTemplate;
    private LessonMapper lessonMapper;
    @Autowired
    private GroupDao groupDao;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonMapper = lessonMapper;
    }

    @Transactional
    public void create(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lesson.getClassroom().getId());
            statement.setInt(2, lesson.getCourse().getId());
            statement.setInt(3, lesson.getTeacher().getId());
            statement.setObject(4, lesson.getDate());
            statement.setInt(5, lesson.getTime().getId());
            return statement;
        }, keyHolder);
        lesson.setId((int) keyHolder.getKeys().get("id"));
        setGroups(lesson, new ArrayList<>());
    }

    public Optional<Lesson> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_LESSON, lessonMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void update(Lesson lesson) {
        jdbcTemplate.update(SQL_UPDATE_LESSON,
            lesson.getClassroom().getId(),
            lesson.getCourse().getId(),
            lesson.getTeacher().getId(),
            lesson.getDate(),
            lesson.getTime().getId(),
            lesson.getId());
        List<Group> savedGroups = groupDao.getByLessonId(lesson.getId());
        deleteGroups(lesson, savedGroups);
        setGroups(lesson, savedGroups);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, lessonMapper);
    }

    @Override
    public List<Lesson> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(SQL_FIND_LESSONS_BY_TEACHER, lessonMapper, teacherId);
    }

    @Override
    public List<Lesson> getByClassroomId(int classroomId) {
        return jdbcTemplate.query(SQL_FIND_LESSONS_BY_CLASSROOM, lessonMapper, classroomId);
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_DATE_AND_TIME_AND_TEACHER, lessonMapper, date,
            time.getId(), teacher.getId()));
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_BY_DATE_AND_TIME_AND_CLASSROOM, lessonMapper, date,
                time.getId(), classroom.getId()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> getByDateAndTime(LocalDate date, Time time) {
        return jdbcTemplate.query(SQL_FIND_BY_DATE_AND_TIME, lessonMapper, date,
            time.getId());
    }

    private void setGroups(Lesson lesson, List<Group> groups) {
        lesson.getGroups().stream()
            .filter(group -> !groups.contains(group))
            .forEach(group -> jdbcTemplate.update(SQL_ADD_GROUP, lesson.getId(), group.getId()));
    }

    private void deleteGroups(Lesson lesson, List<Group> groups) {
        groups.stream()
            .filter(group -> !lesson.getGroups().contains(group))
            .forEach(group -> jdbcTemplate.update(SQL_DELETE_GROUP, lesson.getId(), group.getId()));
    }
}
