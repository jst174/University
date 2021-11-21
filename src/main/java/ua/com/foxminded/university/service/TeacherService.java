package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

import static java.lang.String.format;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public void create(Teacher teacher) throws NotUniqueNameException {
        logger.debug("Creating teacher {} {}", teacher.getFirstName(), teacher.getLastName());
        verifyNameUniqueness(teacher);
        teacherDao.create(teacher);
    }

    public Teacher getById(int id) throws EntityNotFoundException {
        logger.debug("Getting teacher with id = {}", id);
        return teacherDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Teacher with id = %s not found", id)));
    }

    public void update(Teacher teacher) throws NotUniqueNameException {
        logger.debug("Updating teacher with id = {}", teacher.getId());
        verifyNameUniqueness(teacher);
        teacherDao.update(teacher);
    }

    public void delete(int id) {
        logger.debug("Deleting teacher with id = {}", id);
        teacherDao.delete(id);
    }

    public List<Teacher> getAll() {
        logger.debug("Getting all teacher");
        return teacherDao.getAll();
    }

    private void verifyNameUniqueness(Teacher teacher) throws NotUniqueNameException {
        if (teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())
            .filter(t -> t.getId() != teacher.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Teacher with name %s %s already exist",
                teacher.getFirstName(), teacher.getLastName()));
        }
    }
}
