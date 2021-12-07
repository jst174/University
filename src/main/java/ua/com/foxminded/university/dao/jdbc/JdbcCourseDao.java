package ua.com.foxminded.university.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.mapper.CourseMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcCourseDao implements CourseDao {

    private static final String SQL_INSERT_COURSE = "INSERT INTO courses (name) values (?)";
    private static final String SQL_FIND_COURSE = "SELECT * FROM courses WHERE id = ?";
    private static final String SQL_UPDATE_COURSE = "UPDATE courses SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_COURSE = "DELETE FROM courses WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM courses";
    private static final String SQL_FIND_TEACHER_COURSES = "SELECT id, name, teacher_id FROM courses INNER JOIN teachers_courses ON id = course_id WHERE teacher_id = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM courses WHERE name = ?";

    private CourseMapper courseMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcCourseDao(JdbcTemplate jdbcTemplate, CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseMapper = courseMapper;
    }

    public void create(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_COURSE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, course.getName());
            return statement;
        }, keyHolder);
        course.setId((int) keyHolder.getKeys().get("id"));
    }

    public Optional<Course> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_COURSE, courseMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Course course) {
        jdbcTemplate.update(SQL_UPDATE_COURSE, course.getName(), course.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_COURSE, id);
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, courseMapper);
    }

    @Override
    public List<Course> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(SQL_FIND_TEACHER_COURSES, courseMapper, teacherId);
    }

    @Override
    public Optional<Course> getByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, courseMapper, name));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        int totalRows = jdbcTemplate.query(SQL_FIND_ALL, courseMapper).size();
        int pageSize = pageable.getPageSize();
        List<Course> courses = jdbcTemplate.query("SELECT * FROM courses LIMIT " + pageSize
            + " OFFSET " + pageable.getOffset(), courseMapper);
        return new PageImpl<Course>(courses, PageRequest.of(pageable.getPageNumber(), pageSize), totalRows);
    }
}
