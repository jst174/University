package ua.com.foxminded.university.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.mapper.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class JdbcClassroomDao implements ClassroomDao {

    private static final String SQL_INSERT_CLASSROOM = "INSERT INTO classrooms (number, capacity) values (?,?)";
    private static final String SQL_FIND_CLASSROOM = "SELECT * FROM classrooms WHERE id = ?";
    private static final String SQL_UPDATE_CLASSROOM = "UPDATE classrooms SET number = ? ,capacity = ? WHERE id = ?";
    private static final String SQL_DELETE_CLASSROOM = "DELETE FROM classrooms WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM classrooms";
    private static final String SQL_FIND_BY_NUMBER = "SELECT * FROM classrooms WHERE number = ?";

    private ClassroomMapper classroomMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcClassroomDao(JdbcTemplate jdbcTemplate, ClassroomMapper classroomMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.classroomMapper = classroomMapper;
    }

    public void create(Classroom classroom) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT_CLASSROOM, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, classroom.getNumber());
                statement.setInt(2, classroom.getCapacity());
                return statement;
            }, keyHolder);
            classroom.setId((int) keyHolder.getKeys().get("id"));
        } catch (DataAccessException e) {
            String msg = format("Couldn't create classroom %s", classroom.getNumber());
            throw new DaoException(msg, e);
        }
    }

    public Optional<Classroom> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_CLASSROOM, classroomMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Classroom classroom) {
        try {
            jdbcTemplate.update(SQL_UPDATE_CLASSROOM, classroom.getNumber(), classroom.getCapacity(), classroom.getId());
        } catch (DataAccessException e) {
            String msg = format("Couldn't update classroom %s", classroom.getNumber());
            throw new DaoException(msg, e);
        }
    }

    public void delete(int id) {
        try {
            jdbcTemplate.update(SQL_DELETE_CLASSROOM, id);
        } catch (DataAccessException e) {
            String msg = format("Couldn't delete classroom with id = %s", id);
            throw new DaoException(msg, e);
        }
    }

    @Override
    public List<Classroom> getAll() {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL, classroomMapper);
        } catch (DataAccessException e) {
            String msg = "Couldn't find all classroom";
            throw new DaoException(msg, e);
        }
    }

    @Override
    public Optional<Classroom> findByNumber(int number) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_BY_NUMBER, classroomMapper, number));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
