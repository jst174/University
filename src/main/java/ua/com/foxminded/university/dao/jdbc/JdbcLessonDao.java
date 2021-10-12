package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;

import java.sql.*;
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
    private static final String SQL_FIND_GROUP = "SELECT * FROM lessons_groups WHERE lesson_id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM lessons_groups WHERE lesson_id = ? and group_id = ?";

    private JdbcTemplate jdbcTemplate;
    private LessonMapper lessonMapper;
    @Autowired
    private JdbcGroupDao groupDao;

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
        setGroups(lesson);
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
        deleteGroups(lesson);
        setUpdatedGroups(lesson);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, lessonMapper);
    }

    @Override
    @Transactional
    public List<Group> getGroups(int lessonId) {
        return jdbcTemplate.query(SQL_FIND_GROUP, new RowMapper<Group>() {
            @Override
            public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
                return groupDao.getById(rs.getInt("group_id"));
            }
        }, lessonId);
    }

    private void setGroups(Lesson lesson) {
        List<Group> groups = lesson.getGroups();
        jdbcTemplate.batchUpdate(SQL_ADD_GROUP, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Group group = groups.get(i);
                ps.setInt(1, lesson.getId());
                ps.setInt(2, group.getId());
            }

            @Override
            public int getBatchSize() {
                return groups.size();
            }
        });
    }

    private void setUpdatedGroups(Lesson lesson) {
        List<Group> groups = getGroups(lesson.getId());
        List<Group> updatedGroups = lesson.getGroups();
        for (Group group : updatedGroups) {
            if (!groups.contains(group)) {
                jdbcTemplate.update(SQL_ADD_GROUP, lesson.getId(), group.getId());
            }
        }
    }

    private void deleteGroups(Lesson lesson) {
        List<Group> groups = getGroups(lesson.getId());
        List<Group> updatedGroups = lesson.getGroups();
        for (Group group : groups) {
            if (!updatedGroups.contains(group)) {
                jdbcTemplate.update(SQL_DELETE_GROUP, lesson.getId(), group.getId());
            }
        }
    }
}
