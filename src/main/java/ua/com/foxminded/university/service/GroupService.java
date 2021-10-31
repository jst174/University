package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;

@Service
public class GroupService {

    private GroupDao groupDao;
    private LessonDao lessonDao;

    public GroupService(GroupDao groupDao, LessonDao lessonDao) {
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
    }

    public void create(Group group) {
        if (!isUnique(group)) {
            groupDao.create(group);
        }
    }

    public Group getById(int id) {
        return groupDao.getById(id);
    }

    public void update(Group group) {
        if (groupDao.getById(group.getId()).equals(group)) {
            groupDao.update(group);
        }
    }

    public void delete(int id) {
        groupDao.delete(id);
    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public List<Group> getByLessonId(int lessonId) {
        return groupDao.getByLessonId(lessonId);
    }

    private boolean isUnique(Group group) {
        return !groupDao.getByName(group.getName()).equals(group);
    }
}
