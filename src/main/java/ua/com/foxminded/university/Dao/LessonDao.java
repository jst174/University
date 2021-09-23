package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Lesson;

@Component
public class LessonDao {

    private static final String SQL_INSERT_LESSON =
        "INSERT INTO lessons (group_id, classroom_id, course_id, teacher_id, lesson_date, lesson_time)" +
            "values (?,?,?,?,?,?)";
    private static final String SQL_FIND_LESSON = "SELECT * FROM lessons WHERE lesson_id = ?";
    private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET group_id = ?, classroom_id = ?," +
        "course_id = ?, teacher_id = ?, lesson_date = ?, lesson_time = ?";
    private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE lesson_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Lesson lesson){
        jdbcTemplate.update(SQL_INSERT_LESSON,
            lesson.getGroups())
    }
}
