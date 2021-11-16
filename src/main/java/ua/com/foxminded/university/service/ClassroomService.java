package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.exceptions.DaoException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.exceptions.ServiceException;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;

import static java.lang.String.format;

@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);
    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    public void createClassroom(Classroom classroom) throws NotUniqueNameException {
            logger.debug("Creating classroom '{}'", classroom.getNumber());
            if (isUnique(classroom)) {
                classroomDao.create(classroom);
            }
    }

    public Classroom getById(int id) throws ServiceException {
        try {
            logger.debug("Getting classroom  with id - '{}'", id);
            return classroomDao.getById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            String msg = format("Classroom with id %s not found", id);
            throw new ServiceException(msg);
        }
    }

    public void update(Classroom classroom) throws NotUniqueNameException {
            logger.debug("Updating classroom '{}'", classroom.getNumber());
            if (isUnique(classroom)) {
                classroomDao.update(classroom);
            }
    }

    public void delete(int id) {
        logger.debug("Deleting classroom with id - '{}'", id);
        classroomDao.delete(id);
    }

    public List<Classroom> getAll() {
        logger.debug("Getting all classrooms");
        return classroomDao.getAll();
    }

    private boolean isUnique(Classroom classroom) throws NotUniqueNameException {
        Optional<Classroom> classroomByNumber = classroomDao.findByNumber(classroom.getNumber());
        Optional<Classroom> classroomById = classroomDao.getById(classroom.getId());
        if ((classroomById.isPresent() && classroomByNumber.orElseThrow().getId() != classroom.getId())
            || (classroomById.isEmpty() && classroomByNumber.isPresent())) {
            String msg = format("Classroom with number %s already exist", classroom.getNumber());
            throw new NotUniqueNameException(msg);
        } else {
            return true;
        }
    }
}
