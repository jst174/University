package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;

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
        if (!groupIsExist(group)) {
            groupDao.create(group);
        } else {
            throw new IllegalArgumentException("group with this name already exist");
        }

    }

    public Group getById(int id) {
        if (idIsExist(id)) {
            return groupDao.getById(id);
        } else {
            throw new IllegalArgumentException("group is not found");
        }

    }

    public void update(Group group) {
        if (idIsExist(group.getId())) {
            groupDao.update(group);
        } else {
            throw new IllegalArgumentException("group is not found");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            groupDao.delete(id);
        } else {
            throw new IllegalArgumentException("group is not found");
        }

    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public List<Group> getByLessonId(int lessonId) {
        if (lessonIdIsExist(lessonId)) {
            return groupDao.getByLessonId(lessonId);
        } else {
            throw new IllegalArgumentException("lesson is not found");
        }

    }

    private boolean groupIsExist(Group group) {
        List<Group> groups = groupDao.getAll();
        return groups.stream().anyMatch(group::equals);
    }

    private boolean idIsExist(int id) {
        List<Group> groups = groupDao.getAll();
        return groups.stream().anyMatch(group -> group.getId() == id);
    }

    private boolean lessonIdIsExist(int id) {
        List<Lesson> lessons = lessonDao.getAll();
        return lessons.stream().anyMatch(lesson -> lesson.getId() == id);
    }
}
