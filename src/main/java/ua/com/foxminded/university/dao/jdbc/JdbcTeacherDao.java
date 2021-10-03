package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Teacher;

import java.sql.Date;
import java.sql.PreparedStatement;
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

    private static final String SQL_ADD_VACATION = "INSERT INTO teachers_vacations(teacher_id, vacation_id) VALUES (?,?)";
    private static final String SQL_ADD_COURSE = "INSERT INTO teachers_courses(teacher_id, course_id) VALUES(?,?)";
    private static final String SQL_FIND_ALl = "SELECT * FROM teachers";

    private TeacherMapper teacherMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    public void create(Teacher teacher, int addressId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            statement.setDate(3, Date.valueOf(teacher.getBirthDate()));
            statement.setString(4, teacher.getGender().toString());
            statement.setInt(5, addressId);
            statement.setString(6, teacher.getPhoneNumber());
            statement.setString(7, teacher.getEmail());
            statement.setString(8, teacher.getAcademicDegree().toString());
            return statement;
        }, keyHolder);
        teacher.setId((int)keyHolder.getKeys().get("id"));
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TEACHER, teacherMapper, id);
    }

    public void addVacation(int teacherId, int vacationId ){
        jdbcTemplate.update(SQL_ADD_VACATION, teacherId, vacationId);
    }

    public void addCourse(int teacherId, int courseId){
        jdbcTemplate.update(SQL_ADD_COURSE, teacherId, courseId);
    }

    public void update(int id, Teacher teacher) {
        jdbcTemplate.update(SQL_UPDATE_TEACHER,
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getBirthDate(),
            teacher.getGender().toString(),
            teacher.getAdress().getId(),
            teacher.getPhoneNumber(),
            teacher.getEmail(),
            teacher.getAcademicDegree().toString(),
            id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALl, teacherMapper);
    }
}
