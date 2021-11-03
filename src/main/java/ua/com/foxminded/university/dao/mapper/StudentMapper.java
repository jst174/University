package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private GroupDao groupDao;

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student(rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getObject("birthday", LocalDate.class),
            Gender.valueOf(rs.getString("gender")),
            addressDao.getById(rs.getInt("address_id")).get(),
            rs.getString("phone_number"),
            rs.getString("email"));
        groupDao.getById(rs.getInt("group_id")).ifPresent(student::setGroup);
        student.setId(rs.getInt("id"));
        return student;
    }
}

