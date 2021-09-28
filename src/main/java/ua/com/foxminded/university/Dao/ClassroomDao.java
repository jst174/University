package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;

@Component
public class ClassroomDao {

    private static final String SQL_INSERT_CLASSROOM = "INSERT INTO classrooms (number, capacity) values (?,?)";
    private static final String SQL_FIND_CLASSROOM = "SELECT * FROM classrooms WHERE classroom_id = ?";
    private static final String SQL_UPDATE_CLASSROOM = "UPDATE classrooms SET number = ? ,capacity = ? WHERE classroom_id = ?";
    private static final String SLQ_DELETE_CLASSROOM = "DELETE FROM classrooms WHERE classroom_id = ?";

    @Autowired
    private ClassroomMapper classroomMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassroomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Classroom classroom) {
        jdbcTemplate.update(SQL_INSERT_CLASSROOM, classroom.getNumber(), classroom.getCapacity());
    }

    public Classroom getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_CLASSROOM, classroomMapper, id);
    }

    public void update(int id, Classroom classroom) {
        jdbcTemplate.update(SQL_UPDATE_CLASSROOM, classroom.getNumber(), classroom.getCapacity(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SLQ_DELETE_CLASSROOM, id);
    }
}
