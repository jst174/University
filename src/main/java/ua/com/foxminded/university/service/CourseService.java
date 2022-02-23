package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Course;

import java.util.List;

import static java.lang.String.format;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void create(Course course) throws NotUniqueNameException {
        logger.debug("Creating course with name = {}", course.getName());
        verifyNameUniqueness(course);
        courseDao.save(course);
    }

    public Course getById(int id) throws EntityNotFoundException {
        logger.debug("Getting course with id = {}", id);
        return courseDao.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Course with id = %s not found", id)));
    }

    public void update(Course course) throws NotUniqueNameException {
        logger.debug("Updating course with id = {}", course.getId());
        verifyNameUniqueness(course);
        courseDao.save(course);
    }

    public void delete(int id) {
        logger.debug("Deleting course with id = {}", id);
        courseDao.deleteById(id);
    }

    public Page<Course> getAll(Pageable pageable) {
        logger.debug("Getting all course");
        return courseDao.findAll(pageable);
    }

    public List<Course> getAll() {
        logger.debug("Getting all course");
        return courseDao.findAll();
    }

    private void verifyNameUniqueness(Course course) throws NotUniqueNameException {
        if (courseDao.findByName(course.getName())
            .filter(c -> c.getId() != course.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Course with name = %s already exist", course.getName()));
        }
    }
}
