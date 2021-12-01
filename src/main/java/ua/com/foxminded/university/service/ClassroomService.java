package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;

import java.util.Collections;
import java.util.List;

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
        logger.debug("Creating classroom with number = {}", classroom.getNumber());
        verifyNameUniqueness(classroom);
        classroomDao.create(classroom);
    }

    public Classroom getById(int id) throws EntityNotFoundException {
        logger.debug("Getting classroom  with id = {}", id);
        return classroomDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Classroom with id = %s not found", id)));
    }

    public void update(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Updating classroom with id = {}", classroom.getId());
        verifyNameUniqueness(classroom);
        classroomDao.update(classroom);
    }

    public void delete(int id) {
        logger.debug("Deleting classroom with id = {}", id);
        classroomDao.delete(id);
    }

    public List<Classroom> getAll() {
        logger.debug("Getting all classrooms");
        return classroomDao.getAll();
    }

    public Page<Classroom> findPaginated(Pageable pageable) {
        List<Classroom> classrooms = classroomDao.getAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Classroom> list;
        if (classrooms.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, classrooms.size());
            list = classrooms.subList(startItem, toIndex);
        }
        return new PageImpl<Classroom>(list, PageRequest.of(currentPage, pageSize), classrooms.size());
    }

    private void verifyNameUniqueness(Classroom classroom) throws NotUniqueNameException {
        if (classroomDao.findByNumber(classroom.getNumber())
            .filter(c -> c.getId() != classroom.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Classroom with number = %s already exist", classroom.getNumber()));
        }
    }
}
