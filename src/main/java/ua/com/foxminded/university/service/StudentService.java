package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Service
public class StudentService {

    private StudentDao studentDao;
    @Value("${max.group.size}")
    private int maxGroupSize;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void create(Student student) {
        if ((isUnique(student)) && (isGroupAvailable(student.getGroup()))) {
            studentDao.create(student);
        }
    }

    public Student getById(int id) {
        return studentDao.getById(id).orElseThrow();
    }

    public void update(Student student) {
        if ((isUnique(student)) && (isGroupAvailable(student.getGroup()))) {
            studentDao.update(student);
        }
    }

    public void delete(int id) {
        studentDao.delete(id);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public boolean isUnique(Student student) {
        if (studentDao.getById(student.getId()).isEmpty()) {
            return studentDao.getByName(student.getFirstName(), student.getLastName()).isEmpty();
        } else {
            return studentDao.getByName(student.getFirstName(), student.getLastName())
                .get()
                .getId() == student.getId();
        }
    }

    private boolean isGroupAvailable(Group group) {
        return studentDao.getByGroupId(group.getId()).size() < maxGroupSize;
    }
}
