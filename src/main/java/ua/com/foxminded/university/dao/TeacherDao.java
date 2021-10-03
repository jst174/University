package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

public interface TeacherDao {
    void create(Teacher teacher, int addressId);

    Teacher getById(int id);

    void update(int id, Teacher teacher);

    void delete(int id);

    List<Teacher> getAll();

    void addVacation(int teacherId, int vacationId);

    void addCourse(int teacherId, int courseId);
}
