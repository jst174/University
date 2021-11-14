package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);
    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    public void createClassroom(Classroom classroom) {
        try {
            logger.debug("Creating classroom '{}'", classroom.getNumber());
            if (isUnique(classroom)) {
                classroomDao.create(classroom);
            } else {
                String msg = format("Classroom with number %s already exist", classroom.getNumber());
                throw new ServiceException(msg);
            }
        } catch (DaoException e) {
            String msg = format("Couldn't create classroom %s", classroom.getNumber());
            throw new ServiceException(msg);
        }

    }

    public Classroom getById(int id) {
        try {
            logger.debug("Getting classroom  with id - '{}'", id);
            return classroomDao.getById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            String msg = format("Classroom with id %s not found", id);
            throw new ServiceException(msg);
        }
    }

    public void update(Classroom classroom) {
        try {
            logger.debug("Updating classroom '{}'", classroom.getNumber());
            if (isUnique(classroom)) {
                classroomDao.update(classroom);
            } else {
                String msg = format("Classroom with number %s already exist", classroom.getNumber());
                throw new ServiceException(msg);
            }
        } catch (DaoException e) {
            String msg = format("Classroom with number %s already exist", classroom.getNumber());
            throw new ServiceException(msg);
        }
    }

    public void delete(int id) {
        try {
            logger.debug("Deleting classroom with id - '{}'", id);
            classroomDao.delete(id);
        } catch (DaoException e) {
            String msg = format("Couldn't delete classroom with id = %s", id);
            throw new ServiceException(msg);
        }
    }

    public List<Classroom> getAll() {
        try {
            logger.debug("Getting all classrooms");
            return classroomDao.getAll();
        } catch (DaoException e) {
            String msg = "Couldn't find all classroom";
            throw new ServiceException(msg);
        }
    }

    private boolean isUnique(Classroom classroom) {
        if (classroomDao.getById(classroom.getId()).isEmpty()) {
            return classroomDao.findByNumber(classroom.getNumber()).isEmpty();
        } else {
            return classroomDao.findByNumber(classroom.getNumber()).get().getId() == classroom.getId();
        }
    }
}
