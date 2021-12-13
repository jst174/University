package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    private static final String SQL_GET_BY_FULL_NAME = "SELECT * FROM teachers WHERE first_name = ? and last_name = ?";
    private static final String SQL_COUNT_ROWS = "SELECT COUNT(*) FROM teachers";
    private static final String SQL_GET_TEACHERS_PAGE = "SELECT * FROM teachers LIMIT (?) OFFSET (?)";

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

    public Optional<Teacher> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_TEACHER, teacherMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
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

    @Override
    public Optional<Teacher> getByName(String firstName, String lastName) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_BY_FULL_NAME, teacherMapper,
                firstName, lastName));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Teacher> getAll(Pageable pageable) {
        int totalRows = jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);
        int pageSize = pageable.getPageSize();
        List<Teacher> teachers = jdbcTemplate.query(SQL_GET_TEACHERS_PAGE, teacherMapper, pageSize, pageable.getOffset());
        return new PageImpl<Teacher>(teachers, pageable, totalRows);
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
