package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.List;

import static java.lang.String.format;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentDao studentDao;
    @Value("${max.group.size}")
    private int maxGroupSize;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void create(Student student) throws NotUniqueNameException, NotAvailableGroupException {
        logger.debug("Creating student {} {}", student.getFirstName(), student.getLastName());
        verifyNameUniqueness(student);
        verifyGroupAvailability(student.getGroup());
        studentDao.create(student);
    }


    public Student getById(int id) throws EntityNotFoundException {
        logger.debug("Getting student with id = {}", id);
        return studentDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Student with id = %s not found", id)));
    }

    public void update(Student student) throws NotUniqueNameException, NotAvailableGroupException {
        logger.debug("Updating student with id = {}", student.getId());
        verifyNameUniqueness(student);
        verifyGroupAvailability(student.getGroup());
        studentDao.update(student);
    }

    public void delete(int id) {
        logger.debug("Deleting student with id = {}", id);
        studentDao.delete(id);
    }

    public List<Student> getAll() {
        logger.debug("Getting all students");
        return studentDao.getAll();
    }

    public void verifyNameUniqueness(Student student) throws NotUniqueNameException {
        if (studentDao.getByName(student.getFirstName(), student.getLastName())
            .filter(s -> s.getId() != student.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Student with name %s %s already exist",
                student.getFirstName(), student.getLastName()));
        }
    }

    private void verifyGroupAvailability(Group group) throws NotAvailableGroupException {
        int groupSize = studentDao.getByGroupId(group.getId()).size();
        if (groupSize >= maxGroupSize) {
            throw new NotAvailableGroupException(format("Group with name %s not available. " +
                "Max group size = %s has already been reached", group.getName(), groupSize));
        }
    }

}
