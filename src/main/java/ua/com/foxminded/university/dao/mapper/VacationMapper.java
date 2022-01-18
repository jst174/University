package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Vacation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    @Autowired
    private TeacherDao teacherDao;

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vacation vacation = new Vacation();
        vacation.setStart(rs.getObject("start", LocalDate.class));
        vacation.setEnd(rs.getObject("ending", LocalDate.class));
        teacherDao.getById(rs.getInt("teacher_id")).ifPresent(vacation::setTeacher);
        vacation.setId(rs.getInt("id"));
        return vacation;
    }
}
