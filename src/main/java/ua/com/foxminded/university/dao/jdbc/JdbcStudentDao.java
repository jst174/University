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
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Student;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStudentDao implements StudentDao {

    private static final String SQL_INSERT_STUDENT =
        "INSERT INTO students(first_name,last_name,birthday,gender,address_id,phone_number,email, group_id) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_FIND_STUDENT = "SELECT * FROM students WHERE id = ?";
    private static final String SQL_UPDATE_STUDENT = "UPDATE students SET first_name = ?, last_name = ?, " +
        "birthday = ?, gender = ?, address_id = ?, phone_number = ?, email = ?, group_id = ? WHERE id = ?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM students";
    private static final String SQL_GET_BY_GROUP = "SELECT * FROM students WHERE group_id = ?";
    private static final String SQL_GET_BY_FULL_NAME = "SELECT * FROM students WHERE first_name = ? and last_name = ?";
    private static final String SQL_COUNT_ROWS = "SELECT COUNT(*) FROM students";
    private static final String SQL_GET_STUDENTS_PAGE = "SELECT * FROM students LIMIT (?) OFFSET (?)";

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
            statement.setInt(8, student.getGroup().getId());
            return statement;
        }, keyHolder);
        student.setId((int) keyHolder.getKeys().get("id"));
    }

    public Optional<Student> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_STUDENT, studentMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Student updatedStudent) {
        updateAddress(updatedStudent);
        jdbcTemplate.update(SQL_UPDATE_STUDENT,
            updatedStudent.getFirstName(),
            updatedStudent.getLastName(),
            updatedStudent.getBirthDate(),
            updatedStudent.getGender().toString(),
            updatedStudent.getAddress().getId(),
            updatedStudent.getPhoneNumber(),
            updatedStudent.getEmail(),
            updatedStudent.getGroup().getId(),
            updatedStudent.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, studentMapper);
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        return jdbcTemplate.query(SQL_GET_BY_GROUP, studentMapper, groupId);
    }

    @Override
    public Optional<Student> getByName(String firstName, String lastName) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_FULL_NAME, studentMapper,
                firstName, lastName));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        int totalRows = jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);
        int pageSize = pageable.getPageSize();
        List<Student> students = jdbcTemplate.query(SQL_GET_STUDENTS_PAGE, studentMapper, pageSize, pageable.getOffset());
        return new PageImpl<Student>(students, pageable, totalRows);
    }

    private void updateAddress(Student updatedStudent) {
        Student student = jdbcTemplate.queryForObject(SQL_FIND_STUDENT, studentMapper, updatedStudent.getId());
        if (student != null) {
            Address address = student.getAddress();
            Address updatedAddress = updatedStudent.getAddress();
            updatedAddress.setId(address.getId());
            addressDao.update(updatedAddress);
        }
    }
}
