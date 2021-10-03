package ua.com.foxminded.university.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Time;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TimeMapper implements RowMapper<Time> {
    @Override
    public Time mapRow(ResultSet rs, int rowNum) throws SQLException {
        Time time = new Time(rs.getTime("start").toLocalTime(),
            rs.getTime("ending").toLocalTime());
        time.setId(rs.getInt("id"));
        return time;
    }
}
