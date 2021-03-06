package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;

import org.slf4j.Logger;

import java.util.List;

import static java.lang.String.format;

@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);

    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    @Transactional
    public void createClassroom(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Creating classroom with number = {}", classroom.getNumber());
        verifyNameUniqueness(classroom);
        classroomDao.create(classroom);
    }

    @Transactional
    public Classroom getById(int id) throws EntityNotFoundException {
        logger.debug("Getting classroom  with id = {}", id);
        return classroomDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Classroom with id = %s not found", id)));
    }

    @Transactional
    public void update(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Updating classroom with id = {}", classroom.getId());
        verifyNameUniqueness(classroom);
        classroomDao.update(classroom);
    }

    @Transactional
    public void delete(int id) {
        logger.debug("Deleting classroom with id = {}", id);
        classroomDao.delete(id);
    }

    @Transactional
    public Page<Classroom> getAll(Pageable pageable) {
        logger.debug("Getting all classrooms");
        return classroomDao.getAll(pageable);
    }

    @Transactional
    public List<Classroom> getAll() {
        logger.debug("Getting all classrooms");
        return classroomDao.getAll();
    }

    private void verifyNameUniqueness(Classroom classroom) throws NotUniqueNameException {
        if (classroomDao.findByNumber(classroom.getNumber())
            .filter(c -> c.getId() != classroom.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Classroom with number = %s already exist", classroom.getNumber()));
        }
    }
}
