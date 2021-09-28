package ua.com.foxminded.university.Dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.ClassroomDao;
import ua.com.foxminded.university.Dao.CourseDao;
import ua.com.foxminded.university.Dao.TeacherDao;
import ua.com.foxminded.university.Dao.TimeDao;
import ua.com.foxminded.university.model.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private TimeDao timeDao;

    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson(
            courseDao.getById(rs.getInt("course_id")),
            classroomDao.getById(rs.getInt("classroom_id")),
            teacherDao.getById(rs.getInt("teacher_id")),
            rs.getDate("lesson_date").toLocalDate(),
            timeDao.getById(rs.getInt("time_id"))
        );
        lesson.setId(rs.getInt("lesson_id"));
        return lesson;
    }
}
