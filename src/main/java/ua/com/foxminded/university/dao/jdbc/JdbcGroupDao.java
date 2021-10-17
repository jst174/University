package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.mapper.GroupMapper;
import ua.com.foxminded.university.model.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcGroupDao implements GroupDao {

    private static final String SQL_INSERT_GROUP = "INSERT INTO groups (name) VALUES(?)";
    private static final String SQL_FIND_GROUP = "SELECT * FROM groups WHERE id = ?";
    private static final String SQL_UPDATE_GROUP = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM groups";
    private static final String SQL_FIND_LESSON_GROUP = "SELECT id, name, lesson_id FROM groups INNER JOIN lessons_groups ON id = group_id  WHERE lesson_id = ?";

    private JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;

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

    public void update(Group group) {
        jdbcTemplate.update(SQL_UPDATE_GROUP, group.getName(), group.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_GROUP, id);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, groupMapper);
    }


    @Override
    public List<Group> getByLessonId(int lessonId) {
        return jdbcTemplate.query(SQL_FIND_LESSON_GROUP, groupMapper, lessonId);
    }
}
