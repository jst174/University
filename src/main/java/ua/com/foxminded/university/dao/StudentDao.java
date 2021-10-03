package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentDao {
    void create(Student student, int addressId);

    Student getById(int id);

    void update(int id, Student student);

    void delete(int id);

    List<Student> getAll();
}
