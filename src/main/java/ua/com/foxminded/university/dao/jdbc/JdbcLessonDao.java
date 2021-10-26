package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_LESSON, lessonMapper, id);
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
