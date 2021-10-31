package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class StudentService {

    private StudentDao studentDao;
    @Value("${group.capacity}")
    private int groupCapacity;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void create(Student student) {
        if (!isUnique(student) && isGroupAvailable(student.getGroup())) {
            studentDao.create(student);
        }
    }

    public Student getById(int id) {
        return studentDao.getById(id);
    }

    public void update(Student student) {
        if (studentDao.getById(student.getId()).equals(student) && isGroupAvailable(student.getGroup())) {
            studentDao.update(student);
        }

    }

    public void delete(int id) {
        studentDao.delete(id);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    private boolean isUnique(Student student) {
        List<Student> students = studentDao.getAll();
        return students.contains(student);
    }

    private boolean isGroupAvailable(Group group) {
        return studentDao.getByGroupId(group.getId()).size() < groupCapacity;
    }

}
