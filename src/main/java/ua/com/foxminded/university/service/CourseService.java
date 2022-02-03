package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void create(Course course) throws NotUniqueNameException {
        logger.debug("Creating course with name = {}", course.getName());
        verifyNameUniqueness(course);
        courseDao.create(course);
    }

    @Transactional
    public Course getById(int id) throws EntityNotFoundException {
        logger.debug("Getting course with id = {}", id);
        return courseDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Course with id = %s not found", id)));
    }

    @Transactional
    public void update(Course course) throws NotUniqueNameException {
        logger.debug("Updating course with id = {}", course.getId());
        verifyNameUniqueness(course);
        courseDao.update(course);
    }

    @Transactional
    public void delete(int id) {
        logger.debug("Deleting course with id = {}", id);
        courseDao.delete(id);
    }

    @Transactional
    public Page<Course> getAll(Pageable pageable) {
        logger.debug("Getting all course");
        return courseDao.getAll(pageable);
    }

    @Transactional
    public List<Course> getAll() {
        logger.debug("Getting all course");
        return courseDao.getAll();
    }

    private void verifyNameUniqueness(Course course) throws NotUniqueNameException {
        if (courseDao.getByName(course.getName())
            .filter(c -> c.getId() != course.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Course with name = %s already exist", course.getName()));
        }
    }
}
