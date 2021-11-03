package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Autowired
    private AddressDao addressDao;

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher(rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getObject("birthday", LocalDate.class),
            Gender.valueOf(rs.getString("gender")),
            addressDao.getById(rs.getInt("address_id")).get(),
            rs.getString("phone_number"),
            rs.getString("email"),
            AcademicDegree.valueOf(rs.getString("academic_degree")));
        teacher.setId(rs.getInt("id"));
        return teacher;
    }
}
