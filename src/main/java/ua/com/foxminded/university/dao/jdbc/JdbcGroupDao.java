package ua.com.foxminded.university.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Component
public class JdbcGroupDao implements GroupDao {

    private static final String SQL_INSERT_GROUP = "INSERT INTO groups (name) VALUES(?)";
    private static final String SQL_FIND_GROUP = "SELECT * FROM groups WHERE id = ?";
    private static final String SQL_UPDATE_GROUP = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM groups";
    private static final String SQL_FIND_LESSON_GROUP = "SELECT id, name, lesson_id FROM groups INNER JOIN lessons_groups ON id = group_id  WHERE lesson_id = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM groups WHERE name = ?";
    private static final String SQL_COUNT_ROWS = "SELECT COUNT(*) FROM groups";
    private static final String SQL_GET_GROUPS_PAGE = "SELECT * FROM groups LIMIT (?) OFFSET (?)";

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
        }, keyHolder);
        group.setId((int) keyHolder.getKeys().get("id"));
    }

    public Optional<Group> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_GROUP, groupMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
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

    @Override
    public Optional<Group> getByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, groupMapper, name));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        int totalRows = jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);
        int pageSize = pageable.getPageSize();
        List<Group> groups = jdbcTemplate.query(SQL_GET_GROUPS_PAGE, groupMapper, pageSize, pageable.getOffset());
        return new PageImpl<Group>(groups, pageable, totalRows);
    }
}
