package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.model.Lesson;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

    private JdbcTemplate jdbcTemplate;
    private LessonMapper lessonMapper;

    @Autowired
    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonMapper = lessonMapper;
    }

    public void create(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lesson.getClassroom().getId());
            statement.setInt(2, lesson.getCourse().getId());
            statement.setInt(3, lesson.getTeacher().getId());
            statement.setDate(4, Date.valueOf(lesson.getDate()));
            statement.setInt(5, lesson.getTime().getId());
            return statement;
        }, keyHolder);
        lesson.setId((int) keyHolder.getKeys().get("id"));
    }

    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_LESSON, lessonMapper, id);
    }

    @Override
    public void update(Lesson lesson) {
        jdbcTemplate.update(SQL_UPDATE_LESSON,
            lesson.getClassroom().getId(),
            lesson.getCourse().getId(),
            lesson.getTeacher().getId(),
            lesson.getDate(),
            lesson.getTime().getId(),
            lesson.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, lessonMapper);
    }

    @Override
    public void addGroup(int lessonId, int groupId) {
        jdbcTemplate.update(SQL_ADD_GROUP, lessonId, groupId);
    }
}
