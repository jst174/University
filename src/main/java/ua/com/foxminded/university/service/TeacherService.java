package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private TeacherDao teacherDao;
    private AddressDao addressDao;

    public TeacherService(TeacherDao teacherDao, AddressDao addressDao) {
        this.teacherDao = teacherDao;
        this.addressDao = addressDao;
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

    public void update(Teacher updatedTeacher) throws NotUniqueNameException, EntityNotFoundException {
        logger.debug("Updating teacher with id = {}", updatedTeacher.getId());
        verifyNameUniqueness(updatedTeacher);
        updateAddress(updatedTeacher);
        teacherDao.update(updatedTeacher);
    }

    public void delete(int id) {
        logger.debug("Deleting teacher with id = {}", id);
        teacherDao.delete(id);
    }

    public Page<Teacher> getAll(Pageable pageable) {
        logger.debug("Getting all teacher");
        return teacherDao.getAll(pageable);
    }

    private void verifyNameUniqueness(Teacher teacher) throws NotUniqueNameException {
        if (teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())
            .filter(t -> t.getId() != teacher.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Teacher with name %s %s already exist",
                teacher.getFirstName(), teacher.getLastName()));
        }
    }

    private void updateAddress(Teacher updatedTeacher) throws EntityNotFoundException {
        Teacher teacher = getById(updatedTeacher.getId());
        Address address = teacher.getAddress();
        Address updatedAddress = updatedTeacher.getAddress();
        updatedAddress.setId(address.getId());
        addressDao.update(updatedAddress);
    }
}
