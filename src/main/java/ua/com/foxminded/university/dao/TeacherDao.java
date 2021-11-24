package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Teacher;

import java.util.Optional;

public interface TeacherDao extends Dao<Teacher> {

    Optional<Teacher> getByName(String firstName, String lastName);
}
