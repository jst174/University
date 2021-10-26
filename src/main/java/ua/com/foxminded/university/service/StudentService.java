package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Service
public class StudentService {

    private StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void create(Student student) {
        if (!studentIsExist(student)) {
            studentDao.create(student);
        } else {
            throw new IllegalArgumentException("student already exist");
        }

    }

    public Student getById(int id) {
        if (idIsExist(id)) {
            return studentDao.getById(id);
        } else {
            throw new IllegalArgumentException("student is not found");
        }
    }

    public void update(Student student) {
        if (idIsExist(student.getId())) {
            studentDao.update(student);
        } else {
            throw new IllegalArgumentException("student is not found");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            studentDao.delete(id);
        } else {
            throw new IllegalArgumentException("student is not found");
        }

    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    private boolean studentIsExist(Student student) {
        List<Student> students = studentDao.getAll();
        return students.stream().anyMatch(student::equals);
    }

    private boolean idIsExist(int id) {
        List<Student> students = studentDao.getAll();
        return students.stream().anyMatch(student -> student.getId() == id);
    }
}
