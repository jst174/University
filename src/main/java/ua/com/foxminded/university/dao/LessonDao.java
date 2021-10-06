package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    void addGroup(int lessonId, int groupId);

}
