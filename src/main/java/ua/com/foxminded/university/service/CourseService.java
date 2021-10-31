package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Service
public class CourseService {

    private CourseDao courseDao;
    private TeacherDao teacherDao;

    public CourseService(CourseDao courseDao, TeacherDao teacherDao) {
        this.courseDao = courseDao;
        this.teacherDao = teacherDao;
    }

    public void create(Course course) {
        if (!isUnique(course)) {
            courseDao.create(course);
        }
    }

    public Course getById(int id) {
        return courseDao.getById(id);
    }

    public void update(Course course) {
        if (courseDao.getById(course.getId()).equals(course)) {
            courseDao.update(course);
        }
    }

    public void delete(int id) {
        courseDao.delete(id);
    }

    public List<Course> getAll() {
        return courseDao.getAll();
    }

    public List<Course> getByTeacherId(int teacherId) {
        return courseDao.getByTeacherId(teacherId);
    }

    private boolean isUnique(Course course) {
        return !courseDao.getByName(course.getName()).equals(course);
    }
}
