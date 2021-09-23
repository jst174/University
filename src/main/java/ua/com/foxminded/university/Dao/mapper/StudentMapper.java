package ua.com.foxminded.university.Dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.AddressDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Autowired
    private AddressDao addressDao;
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student(rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("birthday").toLocalDate(),
            Gender.valueOf(rs.getString("gender")),
            addressDao.getById(rs.getInt("address_id")),
            rs.getString("phone_number"),
            rs.getString("email"));
        student.setId(rs.getInt("student_id"));
        return student;
    }
}

