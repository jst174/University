package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.model.Address;
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
    private AddressDao addressDao;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    @Transactional
    public void create(Student student) {
        addressDao.create(student.getAddress());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setObject(3, student.getBirthDate());
            statement.setString(4, student.getGender().toString());
            statement.setInt(5, student.getAddress().getId());
            statement.setString(6, student.getPhoneNumber());
            statement.setString(7, student.getEmail());
            return statement;
        }, keyHolder);
        student.setId((int) keyHolder.getKeys().get("id"));
    }

    public Student getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_STUDENT, studentMapper, id);
    }

    public void update(Student student) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT,
            student.getFirstName(),
            student.getLastName(),
            student.getBirthDate(),
            student.getGender().toString(),
            student.getAddress().getId(),
            student.getPhoneNumber(),
            student.getEmail(),
            student.getGroup().getId(),
            student.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, studentMapper);
    }

}
