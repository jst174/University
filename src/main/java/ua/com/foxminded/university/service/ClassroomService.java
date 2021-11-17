package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.exceptions.DaoException;
import ua.com.foxminded.university.exceptions.NoEntityException;
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
        isUnique(classroom);
        classroomDao.create(classroom);
    }

    public Classroom getById(int id) throws NoEntityException {
        logger.debug("Getting classroom  with id - '{}'", id);
        return classroomDao.getById(id).orElseThrow(() ->
            new NoEntityException(format("Classroom with id %s not found", id)));
    }

    public void update(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Updating classroom '{}'", classroom.getNumber());
        isUnique(classroom);
        classroomDao.update(classroom);

    }

    public void delete(int id) {
        logger.debug("Deleting classroom with id - '{}'", id);
        classroomDao.delete(id);
    }

    public List<Classroom> getAll() {
        logger.debug("Getting all classrooms");
        return classroomDao.getAll();
    }

    private void isUnique(Classroom classroom) throws NotUniqueNameException {
        Optional<Classroom> classroomByNumber = classroomDao.findByNumber(classroom.getNumber());
        Optional<Classroom> classroomById = classroomByNumber.filter(classroom1 -> classroom1.getId() == classroom.getId());
        if ((classroomById.isPresent() && classroomByNumber.orElseThrow().getId() != classroom.getId())
            || (classroomById.isEmpty() && classroomByNumber.isPresent())) {
            throw new NotUniqueNameException(format("Classroom with number %s already exist", classroom.getNumber()));
        }
    }
}
