package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.GroupMapper;
import ua.com.foxminded.university.model.Group;

@Component
public class GroupDao {

    private static final String SQL_INSERT_GROUP = "INSERT INTO groups (group_name) values(?)";
    private static final String SQL_FIND_GROUP = "SELECT * FROM groups WHERE group_id = ?";
    private static final String SQL_UPDATE_GROUP = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM groups where group_id = ?";
    private static final String SQL_ADD_STUDENT = "insert into group_students (group_id, student_id) values (?,?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Group group){
        jdbcTemplate.update(SQL_INSERT_GROUP, group.getName());
    }

    public Group getById(int id){
       return jdbcTemplate.queryForObject(SQL_FIND_GROUP, new GroupMapper(), id);
    }

    public void update(int id, Group group){
        jdbcTemplate.update(SQL_UPDATE_GROUP, group.getName(), id);
    }

    public void delete(int id){
        jdbcTemplate.update(SQL_DELETE_GROUP, id);
    }

    public void addStudent(int groupId, int studentId){
        jdbcTemplate.update(SQL_ADD_STUDENT, groupId, studentId);
    }


}
