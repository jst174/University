package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.CourseMapper;
import ua.com.foxminded.university.model.Course;

@Component
public class CourseDao {

    private static final String SQL_INSERT_COURSE = "INSERT INTO courses (course_name) values (?)";
    private static final String SQL_FIND_COURSE = "SELECT * FROM courses WHERE course_id = ?";
    private static final String SQL_UPDATE_COURSE = "UPDATE courses SET course_name = ? WHERE course_id = ?";
    private static final String SQL_DELETE_COURSE = "DELETE FROM courses WHERE course_id = ?";

    @Autowired
    private CourseMapper courseMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Course course) {
        jdbcTemplate.update(SQL_INSERT_COURSE, course.getName());
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
}
