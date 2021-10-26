package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.dao.mapper.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcVacationDao implements VacationDao {

    private static final String SQL_INSERT_VACATION =
        "INSERT INTO vacations (start, ending, teacher_id) VALUES(?,?,?)";
    private static final String SQL_FIND_VACATION = "SELECT * FROM vacations WHERE id = ?";
    private static final String SQL_UPDATE_VACATION =
        "UPDATE vacations SET start = ?, ending = ?, teacher_id = ? WHERE id = ?";
    private static final String SQL_DELETE_VACATION = "DELETE FROM vacations WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM vacations";
    private static final String SQL_FIND_TEACHER_VACATIONS = "SELECT * FROM vacations WHERE teacher_id = ?";

    private VacationMapper vacationMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationMapper vacationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationMapper = vacationMapper;
    }

    public void create(Vacation vacation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_VACATION, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, vacation.getStart());
            statement.setObject(2, vacation.getEnd());
            statement.setInt(3, vacation.getTeacher().getId());
            return statement;
        }, keyHolder);
        vacation.setId((int) keyHolder.getKeys().get("id"));
    }

    public Vacation getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_VACATION, vacationMapper, id);
    }

    public void update(Vacation vacation) {
        jdbcTemplate.update(SQL_UPDATE_VACATION,
            vacation.getStart(),
            vacation.getEnd(),
            vacation.getTeacher().getId(),
            vacation.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_VACATION, id);
    }

    @Override
    public List<Vacation> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, vacationMapper);
    }

    @Override
    public List<Vacation> getByTeacherId(int id) {
        return jdbcTemplate.query(SQL_FIND_TEACHER_VACATIONS, vacationMapper, id);
    }
}
