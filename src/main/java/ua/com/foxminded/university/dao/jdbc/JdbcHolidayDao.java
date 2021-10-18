package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.mapper.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcHolidayDao implements HolidayDao {

    private static final String SQL_INSERT_HOLIDAY = "INSERT INTO holidays (name, date) VALUES (?,?)";
    private static final String SQL_FIND_HOLIDAY = "SELECT * FROM holidays WHERE id = ?";
    private static final String SQL_UPDATE_HOLIDAY = "UPDATE holidays SET name=?, date=? WHERE id = ?";
    private static final String SQL_DELETE_HOLIDAY = "DELETE FROM holidays WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM holidays";

    private HolidayMapper holidayMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate, HolidayMapper holidayMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.holidayMapper = holidayMapper;
    }


    public void create(Holiday holiday) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_HOLIDAY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, holiday.getName());
            statement.setObject(2, holiday.getDate());
            return statement;
        }, keyHolder);
        holiday.setId((int)keyHolder.getKeys().get("id"));
    }

    public Holiday getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_HOLIDAY, holidayMapper, id);
    }

    public void update(Holiday holiday) {
        jdbcTemplate.update(SQL_UPDATE_HOLIDAY, holiday.getName(), holiday.getDate(), holiday.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_HOLIDAY, id);
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, holidayMapper);
    }
}
