package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String SQL_INSERT_ADDRESS =
        "INSERT INTO addresses(country, city, street, house_number, apartment_number, postcode) values (?,?,?,?,?,?)";

    private TeacherMapper teacherMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    public void create(Teacher teacher) {
        createAddress(teacher.getAddress());
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
        setCourses(teacher);
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TEACHER, teacherMapper, id);
    }

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
        setCourses(teacher);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALl, teacherMapper);
    }

    private void createAddress(Address address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getStreet());
            statement.setString(4, address.getHouseNumber());
            statement.setString(5, address.getApartmentNumber());
            statement.setString(6, address.getPostcode());
            return statement;
        }, keyHolder);
        address.setId((int) keyHolder.getKeys().get("id"));
    }

    private void setCourses(Teacher teacher) {
        List<Course> courses = teacher.getCourses();
        jdbcTemplate.batchUpdate(SQL_ADD_COURSE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Course course = courses.get(i);
                ps.setInt(1, teacher.getId());
                ps.setInt(2, course.getId());
            }

            @Override
            public int getBatchSize() {
                return courses.size();
            }
        });
    }

}
