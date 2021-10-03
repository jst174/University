package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.mapper.CourseMapper;
import ua.com.foxminded.university.model.Course;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcCourseDao implements CourseDao {

    private static final String SQL_INSERT_COURSE = "INSERT INTO courses (name) values (?)";
    private static final String SQL_FIND_COURSE = "SELECT * FROM courses WHERE id = ?";
    private static final String SQL_UPDATE_COURSE = "UPDATE courses SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_COURSE = "DELETE FROM courses WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM courses";

    private CourseMapper courseMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCourseDao(JdbcTemplate jdbcTemplate, CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseMapper = courseMapper;
    }

    public void create(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_COURSE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,course.getName());
            return statement;
        }, keyHolder);
        course.setId((int)keyHolder.getKeys().get("id"));
    }

    public Course getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_COURSE, courseMapper, id);
    }

    public void update(int id, Course course) {
        jdbcTemplate.update(SQL_UPDATE_COURSE, course.getName(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_COURSE, id);
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, courseMapper);
    }
}