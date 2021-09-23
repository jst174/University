package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.StudentMapper;
import ua.com.foxminded.university.model.Student;

import java.sql.Date;

@Component
public class StudentDao {

    private static final String SQL_INSERT_STUDENT =
        "INSERT INTO students(first_name,last_name,birthday,gender,address_id,phone_number,email) values (?,?,?,?,?,?,?)";
    private static final String SQL_FIND_STUDENT = "SELECT * FROM students WHERE student_id = ?";
    private static final String SQL_UPDATE_STUDENT = "UPDATE students SET address_id = ?, phone_number = ?, email = ? WHERE student_id = ?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM students WHERE student_id = ?";

    @Autowired
    private StudentMapper studentMapper;

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Student student, int addressId) {
        jdbcTemplate.update(SQL_INSERT_STUDENT,
            student.getFirstName(),
            student.getLastName(),
            Date.valueOf(student.getBirthDate()),
            student.getGender().toString(),
            addressId,
            student.getPhoneNumber(),
            student.getEmail());
    }

    public Student getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_STUDENT, studentMapper, id);
    }

    public void update(Student student) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT,
            student.getAdress().getId(), student.getPhoneNumber(), student.getEmail(), student.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }
}
