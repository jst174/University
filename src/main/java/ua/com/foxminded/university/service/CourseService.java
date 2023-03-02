package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.CourseRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Course;

import java.util.List;

import static java.lang.String.format;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void create(Course course) throws NotUniqueNameException {
        logger.debug("Creating course with name = {}", course.getName());
        verifyNameUniqueness(course);
        courseRepository.save(course);
    }

    public Course getById(int id) throws EntityNotFoundException {
        logger.debug("Getting course with id = {}", id);
        return courseRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Course with id = %s not found", id)));
    }

    public void update(Course course) throws NotUniqueNameException {
        logger.debug("Updating course with id = {}", course.getId());
        verifyNameUniqueness(course);
        courseRepository.save(course);
    }

    public void delete(int id) {
        logger.debug("Deleting course with id = {}", id);
        courseRepository.deleteById(id);
    }

    public Page<Course> getAll(Pageable pageable) {
        logger.debug("Getting all course");
        return courseRepository.findAll(pageable);
    }

    public List<Course> getAll() {
        logger.debug("Getting all course");
        return courseRepository.findAll();
    }

    private void verifyNameUniqueness(Course course) throws NotUniqueNameException {
        if (courseRepository.findByName(course.getName())
            .filter(c -> c.getId() != course.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Course with name = %s already exist", course.getName()));
        }
    }
}
