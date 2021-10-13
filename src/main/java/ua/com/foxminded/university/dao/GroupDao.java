package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    List<Group> getLessonGroups(int lessonId);
}
