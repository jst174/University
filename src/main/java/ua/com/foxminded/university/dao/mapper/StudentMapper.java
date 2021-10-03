package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Autowired
    private JdbcAddressDao addressDao;
    @Autowired
    private JdbcGroupDao groupDao;
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student(rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("birthday").toLocalDate(),
            Gender.valueOf(rs.getString("gender")),
            addressDao.getById(rs.getInt("address_id")),
            rs.getString("phone_number"),
            rs.getString("email"));
        student.setGroup(groupDao.getById(rs.getInt("group_id")));
        student.setId(rs.getInt("id"));
        return student;
    }
}
