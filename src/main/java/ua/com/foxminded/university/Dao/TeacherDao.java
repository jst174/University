package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Teacher;

import java.sql.Date;

@Component
public class TeacherDao {

    private static final String SQL_INSERT_TEACHER =
        "INSERT INTO teachers(first_name,last_name,birthday,gender,address_id,phone_number,email, academic_degree)" +
            "values (?,?,?,?,?,?,?,?)";
    private static final String SQL_FIND_TEACHER = "SELECT * FROM teachers WHERE teacher_id = ?";
    private static final String SQL_UPDATE_TEACHER =
        "UPDATE teachers SET first_name = ?, last_name = ?, birthday = ?, gender = ?, address_id = ?, " +
            "phone_number = ?, email = ?, academic_degree = ? WHERE teacher_id = ?";
    private static final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE teacher_id = ?";

    private static final String SQL_ADD_VACATION = "insert into teachers_vacations(teacher_id, vacation_id) values (?,?)";
    private static final String SQL_ADD_COURSE = "insert into teachers_courses(teacher_id, course_id) values(?,?)";

    @Autowired
    private TeacherMapper teacherMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Teacher teacher, int addressId) {
        jdbcTemplate.update(SQL_INSERT_TEACHER, teacher.getFirstName(),
            teacher.getLastName(),
            Date.valueOf(teacher.getBirthDate()),
            teacher.getGender().toString(),
            addressId,
            teacher.getPhoneNumber(),
            teacher.getEmail(),
            teacher.getAcademicDegree().toString());
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TEACHER, teacherMapper, id);
    }

    public void addVacation(int teacherId, int vacationId ){
        jdbcTemplate.update(SQL_ADD_VACATION, teacherId, vacationId);
    }

    public void addCourse(int teacherId, int courseId){
        jdbcTemplate.update(SQL_ADD_COURSE, teacherId, courseId);
    }

    public void update(int id, Teacher teacher) {
        jdbcTemplate.update(SQL_UPDATE_TEACHER,
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getBirthDate(),
            teacher.getGender().toString(),
            teacher.getAdress().getId(),
            teacher.getPhoneNumber(),
            teacher.getEmail(),
            teacher.getAcademicDegree().toString(),
            id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }
}
