package ua.com.foxminded.university.Dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.university.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group(rs.getString("group_name"));
        group.setId(rs.getInt("group_id"));
        return group;
    }
}
