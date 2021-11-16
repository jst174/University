package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.exceptions.ServiceException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void create(Course course) throws NotUniqueNameException {
            logger.debug("Creating course '{}'", course.getName());
            if (isUnique(course)) {
                courseDao.create(course);
            }
    }

    public Course getById(int id) throws ServiceException {
        try {
            logger.debug("Getting course with id '{}'", id);
            return courseDao.getById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            String msg = format("Course with id %s not found", id);
            throw new ServiceException(msg);
        }
    }

    public void update(Course course) throws NotUniqueNameException {
            logger.debug("Updating course '{}'", course.getName());
            if (isUnique(course)) {
                courseDao.update(course);
            }
    }

    public void delete(int id) {
        logger.debug("Deleting course with id - '{}'", id);
        courseDao.delete(id);
    }

    public List<Course> getAll() {
        logger.debug("Getting all course");
        return courseDao.getAll();
    }

    public List<Course> getByTeacherId(int teacherId) {
        logger.debug("Getting courses by teacher with id - '{}'", teacherId);
        return courseDao.getByTeacherId(teacherId);
    }

    private boolean isUnique(Course course) throws NotUniqueNameException {
        if (courseDao.getById(course.getId()).isEmpty()) {
            return courseDao.getByName(course.getName()).isEmpty();
        } else if (courseDao.getByName(course.getName()).get().getId() == course.getId()) {
            return true;
        } else {
            String msg = format("Course with name %s already exist", course.getName());
            throw new NotUniqueNameException(msg);
        }
    }
}
