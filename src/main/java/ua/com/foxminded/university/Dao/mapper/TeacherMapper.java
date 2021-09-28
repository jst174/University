package ua.com.foxminded.university.Dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.AddressDao;
import ua.com.foxminded.university.Dao.VacationDao;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Autowired
    private AddressDao addressDao;

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher(rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("birthday").toLocalDate(),
            Gender.valueOf(rs.getString("gender")),
            addressDao.getById(rs.getInt("address_id")),
            rs.getString("phone_number"),
            rs.getString("email"),
            AcademicDegree.valueOf(rs.getString("academic_degree")));
        teacher.setId(rs.getInt("teacher_id"));
        return teacher;
    }
}
