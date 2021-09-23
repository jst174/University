package ua.com.foxminded.university.Dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Vacation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vacation vacation = new Vacation(rs.getDate("start_vacation").toLocalDate(),
            rs.getDate("end_vacation").toLocalDate());
        vacation.setId(rs.getInt("vacation_id"));
        return vacation;
    }
}
