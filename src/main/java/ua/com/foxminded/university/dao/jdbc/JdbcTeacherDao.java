package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private AddressDao addressDao;
    @Autowired
    private CourseDao courseDao;

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
        setCourses(teacher, new ArrayList<>());
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
        List<Course> savedCourses = courseDao.getByTeacherId(teacher.getId());
        deleteCourses(teacher, savedCourses);
        setCourses(teacher, savedCourses);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALl, teacherMapper);
    }


    private void setCourses(Teacher updatedTeacher, List<Course> courses) {
        updatedTeacher.getCourses().stream()
            .filter(course -> !courses.contains(course))
            .forEach(course -> jdbcTemplate.update(SQL_ADD_COURSE, updatedTeacher.getId(), course.getId()));
    }


    private void deleteCourses(Teacher teacher, List<Course> savedCourses) {
        savedCourses.stream().
            filter(course -> !teacher.getCourses().contains(course))
            .forEach(course -> jdbcTemplate.update(SQL_DELETE_COURSE, teacher.getId(), course.getId()));
    }
}
