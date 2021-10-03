package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.model.Student;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcStudentDao implements StudentDao {

    private static final String SQL_INSERT_STUDENT =
        "INSERT INTO students(first_name,last_name,birthday,gender,address_id,phone_number,email) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_FIND_STUDENT = "SELECT * FROM students WHERE id = ?";
    private static final String SQL_UPDATE_STUDENT = "UPDATE students SET first_name = ?, last_name = ?, " +
        "birthday = ?, gender = ?, address_id = ?, phone_number = ?, email = ?, group_id = ? WHERE id = ?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM students";

    private StudentMapper studentMapper;
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    public void create(Student student, int addressId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setDate(3, Date.valueOf(student.getBirthDate()));
            statement.setString(4, student.getGender().toString());
            statement.setInt(5, addressId);
            statement.setString(6, student.getPhoneNumber());
            statement.setString(7, student.getEmail());
            return statement;
        }, keyHolder);
        student.setId((int)keyHolder.getKeys().get("id"));
    }

    public Student getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_STUDENT, studentMapper, id);
    }

    public void update(int id, Student student) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT,
            student.getFirstName(),
            student.getLastName(),
            Date.valueOf(student.getBirthDate()),
            student.getGender().toString(),
            student.getAdress().getId(),
            student.getPhoneNumber(),
            student.getEmail(),
            student.getGroup().getId(),
            id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, studentMapper);
    }
}
