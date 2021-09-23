package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;

@Component
public class HolidayDao {

    private static final String SQL_INSERT_HOLIDAY = "INSERT INTO holidays (holiday_name, holiday_date) value (?,?)";
    private static final String SQL_FIND_HOLIDAY = "SELECT * FROM holidays WHERE holiday_id = ?";
    private static final String SQL_UPDATE_HOLIDAY = "UPDATE holidays SET holiday_date";
    private static final String SQL_DELETE_HOLIDAY = "DELETE FROM holidays WHERE holiday_id = ?";

    @Autowired
    private HolidayMapper holidayMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public HolidayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Holiday holiday) {
        jdbcTemplate.update(SQL_INSERT_HOLIDAY, holiday.getDate());
    }

    public Holiday getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_HOLIDAY, holidayMapper, id);
    }

    public void update(Holiday holiday) {
        jdbcTemplate.update(SQL_UPDATE_HOLIDAY, holiday.getDate());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_HOLIDAY, id);
    }
}
