package ua.com.foxminded.university.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.dao.mapper.TimeMapper;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Time;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcTimeDao implements TimeDao {

    private static final String SQL_INSERT_TIME = "INSERT INTO times (start, ending ) VALUES (?,?)";
    private static final String SQL_FIND_TIME = "SELECT * FROM times WHERE id = ?";
    private static final String SQL_UPDATE_TIME = "UPDATE times SET start = ?, ending = ? WHERE id = ?";
    private static final String SQL_DELETE_TIME = "DELETE FROM times WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM times";
    private static final String SQL_FIND_BY_TIME = "SELECT * FROM times WHERE start = ? and ending = ?";
    private static final String SQL_COUNT_ROWS = "SELECT COUNT(*) FROM times";
    private static final String SQL_GET_TIMES_PAGE = "SELECT * FROM times LIMIT (?) OFFSET (?)";

    private TimeMapper timeMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeDao(JdbcTemplate jdbcTemplate, TimeMapper timeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.timeMapper = timeMapper;
    }

    public void create(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, time.getStartTime());
            statement.setObject(2, time.getEndTime());
            return statement;
        }, keyHolder);
        time.setId((int) keyHolder.getKeys().get("id"));
    }

    public Optional<Time> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_TIME, timeMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
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

    @Override
    public Page<Time> getAll(Pageable pageable) {
        int totalRows = jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);
        int pageSize = pageable.getPageSize();
        List<Time> times = jdbcTemplate.query(SQL_GET_TIMES_PAGE, timeMapper, pageSize, pageable.getOffset());
        return new PageImpl<Time>(times, pageable, totalRows);
    }

    @Override
    public Optional<Time> getByTime(LocalTime start, LocalTime end) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_BY_TIME, timeMapper, start, end));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
