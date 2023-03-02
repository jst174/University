package ua.com.foxminded.university.service;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;

import org.slf4j.Logger;

import java.util.List;

import static java.lang.String.format;

@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);

    private ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public void createClassroom(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Creating classroom with number = {}", classroom.getNumber());
        verifyNameUniqueness(classroom);
        classroomRepository.save(classroom);
    }

    public Classroom getById(int id) throws EntityNotFoundException {
        logger.debug("Getting classroom  with id = {}", id);
        return classroomRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Classroom with id = %s not found", id)));
    }

    public void update(Classroom classroom) throws NotUniqueNameException {
        logger.debug("Updating classroom with id = {}", classroom.getId());
        verifyNameUniqueness(classroom);
        classroomRepository.save(classroom);
    }

    public void delete(int id) {
        logger.debug("Deleting classroom with id = {}", id);
        classroomRepository.deleteById(id);
    }

    public Page<Classroom> getAll(Pageable pageable) {
        logger.debug("Getting all classrooms");
        return classroomRepository.findAll(pageable);
    }

    public List<Classroom> getAll() {
        logger.debug("Getting all classrooms");
        return classroomRepository.findAll();
    }

    private void verifyNameUniqueness(Classroom classroom) throws NotUniqueNameException {
        if (classroomRepository.findByNumber(classroom.getNumber())
            .filter(c -> c.getId() != classroom.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Classroom with number = %s already exist", classroom.getNumber()));
        }
    }
}
