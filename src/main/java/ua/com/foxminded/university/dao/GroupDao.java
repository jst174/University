package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;

import java.util.List;

public interface GroupDao {

    void create(Group group);

    Group getById(int id);

    void update(int id, Group group);

    void delete(int id);

    List<Group> getAll();
}
