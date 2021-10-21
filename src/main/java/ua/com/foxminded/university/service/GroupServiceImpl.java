package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDao groupDao;

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public void create(Group group) {
        groupDao.create(group);
    }

    @Override
    public Group getById(int id) {
        return groupDao.getById(id);
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    public void delete(int id) {
        groupDao.delete(id);
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public List<Group> getByLessonId(int lessonId) {
        return groupDao.getByLessonId(lessonId);
    }
}
