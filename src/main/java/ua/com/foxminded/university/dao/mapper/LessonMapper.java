package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.*;
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
    private CourseDao courseDao;
    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private GroupDao groupDao;

    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        courseDao.getById(rs.getInt("course_id")).ifPresent(lesson::setCourse);
        classroomDao.getById(rs.getInt("classroom_id")).ifPresent(lesson::setClassroom);
        teacherDao.getById(rs.getInt("teacher_id")).ifPresent(lesson::setTeacher);
        lesson.setDate(rs.getObject("date", LocalDate.class));
        timeDao.getById(rs.getInt("time_id")).ifPresent(lesson::setTime);
        lesson.setGroups(groupDao.getByLessonId(rs.getInt("id")));
        lesson.setId(rs.getInt("id"));
        return lesson;
    }
}
