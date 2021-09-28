package ua.com.foxminded.university.Dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Holiday;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HolidayMapper implements RowMapper<Holiday> {

    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        Holiday holiday = new Holiday(rs.getString("holiday_name"),
            rs.getDate("holiday_date").toLocalDate());
        holiday.setId(rs.getInt("holiday_id"));
        return holiday;
    }
}
