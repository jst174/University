package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.UniversityConfigProperties;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import static java.lang.String.format;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentDao studentDao;
    private UniversityConfigProperties universityProperties;

    public StudentService(StudentDao studentDao, UniversityConfigProperties universityProperties) {
        this.studentDao = studentDao;
        this.universityProperties = universityProperties;
    }

    @Transactional
    public void create(Student student) throws NotUniqueNameException, NotAvailableGroupException {
        logger.debug("Creating student {} {}", student.getFirstName(), student.getLastName());
        verifyNameUniqueness(student);
        verifyGroupAvailability(student.getGroup());
        studentDao.create(student);
    }

    @Transactional
    public Student getById(int id) throws EntityNotFoundException {
        logger.debug("Getting student with id = {}", id);
        return studentDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Student with id = %s not found", id)));
    }

    @Transactional
    public void update(Student updatedStudent) throws NotUniqueNameException, NotAvailableGroupException, EntityNotFoundException {
        logger.debug("Updating student with id = {}", updatedStudent.getId());
        verifyNameUniqueness(updatedStudent);
        verifyGroupAvailability(updatedStudent.getGroup());
        studentDao.update(updatedStudent);
    }

    @Transactional
    public void delete(int id) {
        logger.debug("Deleting student with id = {}", id);
        studentDao.delete(id);
    }

    @Transactional
    public Page<Student> getAll(Pageable pageable) {
        logger.debug("Getting all students");
        return studentDao.getAll(pageable);
    }

    private void verifyNameUniqueness(Student student) throws NotUniqueNameException {
        if (studentDao.getByFirstNameAndLastName(student.getFirstName(), student.getLastName())
            .filter(s -> s.getId() != student.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Student with name %s %s already exist",
                student.getFirstName(), student.getLastName()));
        }
    }

    private void verifyGroupAvailability(Group group) throws NotAvailableGroupException {
        int groupSize = group.getStudents().size();
        if (groupSize >= universityProperties.getMaxGroupSize()) {
            throw new NotAvailableGroupException(format("Group with name %s not available. " +
                "Max group size = %s has already been reached", group.getName(), groupSize));
        }
    }
}
