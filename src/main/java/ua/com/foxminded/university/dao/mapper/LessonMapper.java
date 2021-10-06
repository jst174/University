package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimeDao;
import ua.com.foxminded.university.model.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Autowired
    private JdbcCourseDao courseDao;
    @Autowired
    private JdbcClassroomDao classroomDao;
    @Autowired
    private JdbcTeacherDao teacherDao;
    @Autowired
    private JdbcTimeDao timeDao;

    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson(
            courseDao.getById(rs.getInt("id")),
            classroomDao.getById(rs.getInt("id")),
            teacherDao.getById(rs.getInt("id")),
            rs.getObject("date", LocalDate.class),
            timeDao.getById(rs.getInt("id"))
        );
        lesson.setId(rs.getInt("id"));
        return lesson;
    }
}
