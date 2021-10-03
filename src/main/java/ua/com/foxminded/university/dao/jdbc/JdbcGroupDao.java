package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.mapper.GroupMapper;
import ua.com.foxminded.university.model.Group;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcGroupDao implements GroupDao {

    private static final String SQL_INSERT_GROUP = "INSERT INTO groups (name) VALUES(?)";
    private static final String SQL_FIND_GROUP = "SELECT * FROM groups WHERE id = ?";
    private static final String SQL_UPDATE_GROUP = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    private static final String SQL_ADD_STUDENT = "INSERT INTO group_students (group_id, student_id) VALUES (?,?)";
    private static final String SQL_FIND_ALL = "SELECT * FROM groups";

    private JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;

    @Autowired
    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
    }

    public void create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.getName());
            return statement;
        },keyHolder);
        group.setId((int)keyHolder.getKeys().get("id"));
    }

    public Group getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_GROUP, groupMapper, id);
    }

    public void update(int id, Group group) {
        jdbcTemplate.update(SQL_UPDATE_GROUP, group.getName(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_GROUP, id);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, groupMapper);
    }

    public void addStudent(int groupId, int studentId) {
        jdbcTemplate.update(SQL_ADD_STUDENT, groupId, studentId);
    }


}