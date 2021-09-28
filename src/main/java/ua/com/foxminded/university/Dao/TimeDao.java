package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.TimeMapper;
import ua.com.foxminded.university.model.Time;

@Component
public class TimeDao {

    private static final String SQL_INSERT_TIME = "INSERT INTO time (start_time, end_time) values (?,?)";
    private static final String SQL_FIND_TIME = "SELECT * FROM time WHERE time_id = ?";
    private static final String SQL_UPDATE_TIME = "UPDATE time SET start_time = ?, end_time = ? WHERE time_id = ?";
    private static final String SQL_DELETE_TIME = "DELETE FROM time WHERE time_id = ?";

    @Autowired
    private TimeMapper timeMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Time time) {
        jdbcTemplate.update(SQL_INSERT_TIME, time.getStartTime(), time.getEndTime());
    }

    public Time getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TIME, timeMapper, id);
    }

    public void update(int id, Time time) {
        jdbcTemplate.update(SQL_UPDATE_TIME, time.getStartTime(),
            time.getEndTime(),
            id);
    }

    public void delete(int id){
        jdbcTemplate.update(SQL_DELETE_TIME, id);
    }


}
