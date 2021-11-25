package ua.com.foxminded.university.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.mapper.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_CLASSROOM, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, classroom.getNumber());
            statement.setInt(2, classroom.getCapacity());
            return statement;
        }, keyHolder);
        classroom.setId((int) keyHolder.getKeys().get("id"));

    }

    public Optional<Classroom> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_CLASSROOM, classroomMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Classroom classroom) {
        jdbcTemplate.update(SQL_UPDATE_CLASSROOM, classroom.getNumber(), classroom.getCapacity(), classroom.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_CLASSROOM, id);
    }

    @Override
    public List<Classroom> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, classroomMapper);
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
