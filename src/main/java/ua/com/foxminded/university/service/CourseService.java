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
        if (!courseIsExist(course)) {
            courseDao.create(course);
        } else {
            throw new IllegalArgumentException("course with this name already exist");
        }
    }

    public Course getById(int id) {
        if (courseIdIsExist(id)) {
            return courseDao.getById(id);
        } else {
            throw new IllegalArgumentException("course is not found");
        }

    }

    public void update(Course course) {
        if (courseIdIsExist(course.getId())) {
            courseDao.update(course);
        } else {
            throw new IllegalArgumentException("course is not found");
        }

    }

    public void delete(int id) {
        if (courseIdIsExist(id)) {
            courseDao.delete(id);
        } else {
            throw new IllegalArgumentException("course is not found");
        }

    }

    public List<Course> getAll() {
        return courseDao.getAll();
    }

    public List<Course> getByTeacherId(int teacherId) {
        if(teacherIdIsExist(teacherId)){
            return courseDao.getByTeacherId(teacherId);
        } else {
            throw new IllegalArgumentException("teacher is not found");
        }

    }

    private boolean courseIsExist(Course course) {
        List<Course> courses = courseDao.getAll();
        return courses.stream().anyMatch(course::equals);
    }

    private boolean courseIdIsExist(int id) {
        List<Course> courses = courseDao.getAll();
        return courses.stream().anyMatch(course -> course.getId() == id);
    }

    private boolean teacherIdIsExist(int id){
        List<Teacher> teachers = teacherDao.getAll();
        return teachers.stream().anyMatch(teacher -> teacher.getId() == id);
    }
}
