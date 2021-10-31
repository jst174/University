package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    List<Student> getByGroupId(int groupId);
}
