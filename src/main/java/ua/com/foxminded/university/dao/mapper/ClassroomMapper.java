package ua.com.foxminded.university.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Classroom;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClassroomMapper implements RowMapper<Classroom> {

    @Override
    public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
        Classroom classroom =  new Classroom(rs.getInt("number"), rs.getInt("capacity"));
        classroom.setId(rs.getInt("id"));
        return classroom;
    }
}
