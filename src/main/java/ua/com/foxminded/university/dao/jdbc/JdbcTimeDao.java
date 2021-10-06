package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.dao.mapper.TimeMapper;
import ua.com.foxminded.university.model.Time;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcTimeDao implements TimeDao {

    private static final String SQL_INSERT_TIME = "INSERT INTO times (start, ending ) VALUES (?,?)";
    private static final String SQL_FIND_TIME = "SELECT * FROM times WHERE id = ?";
    private static final String SQL_UPDATE_TIME = "UPDATE times SET start = ?, ending = ? WHERE id = ?";
    private static final String SQL_DELETE_TIME = "DELETE FROM times WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM times";

    private TimeMapper timeMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTimeDao(JdbcTemplate jdbcTemplate, TimeMapper timeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.timeMapper = timeMapper;
    }

    public void create(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setTime(1, java.sql.Time.valueOf(time.getStartTime()));
            statement.setTime(2, java.sql.Time.valueOf(time.getEndTime()));
            return statement;
        }, keyHolder);
        time.setId((int) keyHolder.getKeys().get("id"));
    }

    public Time getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TIME, timeMapper, id);
    }

    public void update(Time time) {
        jdbcTemplate.update(SQL_UPDATE_TIME, time.getStartTime(),
            time.getEndTime(),
            time.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TIME, id);
    }

    @Override
    public List<Time> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, timeMapper);
    }


}
