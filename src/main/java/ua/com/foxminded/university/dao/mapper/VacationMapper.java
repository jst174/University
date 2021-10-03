package ua.com.foxminded.university.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Vacation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vacation vacation = new Vacation(rs.getDate("start").toLocalDate(),
            rs.getDate("ending").toLocalDate());
        vacation.setId(rs.getInt("id"));
        return vacation;
    }
}
