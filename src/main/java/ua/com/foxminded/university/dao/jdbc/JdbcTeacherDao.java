package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.sql.*;
import java.util.List;

@Component
public class JdbcTeacherDao implements TeacherDao {

    private static final String SQL_INSERT_TEACHER =
        "INSERT INTO teachers(first_name,last_name,birthday,gender,address_id,phone_number,email, academic_degree)" +
            "VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_FIND_TEACHER = "SELECT * FROM teachers WHERE id = ?";
    private static final String SQL_UPDATE_TEACHER =
        "UPDATE teachers SET first_name = ?, last_name = ?, birthday = ?, gender = ?, address_id = ?, " +
            "phone_number = ?, email = ?, academic_degree = ? WHERE id = ?";
    private static final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
    private static final String SQL_ADD_COURSE = "INSERT INTO teachers_courses(teacher_id, course_id) VALUES(?,?)";
    private static final String SQL_FIND_ALl = "SELECT * FROM teachers";
    private static final String SQL_FIND_COURSES = "SELECT * FROM teachers_courses WHERE teacher_id = ?";
    private static final String SQL_DELETE_COURSE = "DELETE FROM teachers_courses WHERE teacher_id = ? and course_id = ?";

    private TeacherMapper teacherMapper;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcAddressDao addressDao;
    @Autowired
    private JdbcCourseDao courseDao;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    @Transactional
    public void create(Teacher teacher) {
        addressDao.create(teacher.getAddress());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            statement.setObject(3, teacher.getBirthDate());
            statement.setString(4, teacher.getGender().toString());
            statement.setInt(5, teacher.getAddress().getId());
            statement.setString(6, teacher.getPhoneNumber());
            statement.setString(7, teacher.getEmail());
            statement.setString(8, teacher.getAcademicDegree().toString());
            return statement;
        }, keyHolder);
        teacher.setId((int) keyHolder.getKeys().get("id"));
        setCourses(teacher, getCourses(teacher));
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TEACHER, teacherMapper, id);
    }

    @Transactional
    public void update(Teacher teacher) {
        jdbcTemplate.update(SQL_UPDATE_TEACHER,
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getBirthDate(),
            teacher.getGender().toString(),
            teacher.getAddress().getId(),
            teacher.getPhoneNumber(),
            teacher.getEmail(),
            teacher.getAcademicDegree().toString(),
            teacher.getId());
        List<Course> courses = getCourses(teacher);
        deleteCourses(teacher, courses);
        setCourses(teacher, courses);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALl, teacherMapper);
    }


    private void setCourses(Teacher teacher, List<Course> courses) {
        List<Course> newCourses = teacher.getCourses();
        if (courses.isEmpty()) {
            jdbcTemplate.batchUpdate(SQL_ADD_COURSE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Course course = newCourses.get(i);
                    ps.setInt(1, teacher.getId());
                    ps.setInt(2, course.getId());
                }

                @Override
                public int getBatchSize() {
                    return newCourses.size();
                }
            });
        } else {
            newCourses.stream()
                .forEach((course) -> {
                    if (courses.stream().noneMatch(course::equals)) {
                        jdbcTemplate.update(SQL_ADD_COURSE, teacher.getId(), course.getId());
                    }
                });
        }

    }

    private void deleteCourses(Teacher teacher, List<Course> courses) {
        courses.stream()
            .forEach((course) -> {
                if (teacher.getCourses().stream().noneMatch(course::equals)) {
                    jdbcTemplate.update(SQL_DELETE_COURSE, teacher.getId(), course.getId());
                }
            });
    }

    private List<Course> getCourses(Teacher teacher){
        return courseDao.getTeacherCourses(teacher.getId());
    }

}
