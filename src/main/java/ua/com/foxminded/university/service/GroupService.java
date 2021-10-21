package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Group;

import java.util.List;

public interface GroupService extends Service<Group> {

    List<Group> getByLessonId(int lessonId);
}
