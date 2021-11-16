package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.exceptions.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private GroupDao groupDao;
    private LessonDao lessonDao;

    public GroupService(GroupDao groupDao, LessonDao lessonDao) {
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
    }

    public void create(Group group) throws NotUniqueNameException {
        logger.debug("Creating group '{}'", group.getName());
        if (isUnique(group)) {
            groupDao.create(group);
        }
    }

    public Group getById(int id) throws ServiceException {
        try {
            logger.debug("Getting group with id '{}'", id);
            return groupDao.getById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            String msg = format("Course with id %s not found", id);
            throw new ServiceException(msg);
        }
    }

    public void update(Group group) throws NotUniqueNameException {
        logger.debug("Updating group '{}'", group.getName());
        if (isUnique(group)) {
            groupDao.update(group);
        }
    }

    public void delete(int id) {
        logger.debug("Deleting group with id '{}'", id);
        groupDao.delete(id);
    }

    public List<Group> getAll() {
        logger.debug("Getting all group");
        return groupDao.getAll();
    }

    public List<Group> getByLessonId(int lessonId) {
        logger.debug("Getting groups by lesson with id '{}'", lessonId);
        return groupDao.getByLessonId(lessonId);
    }

    private boolean isUnique(Group group) throws NotUniqueNameException {
        if (groupDao.getById(group.getId()).isEmpty()) {
            return groupDao.getByName(group.getName()).isEmpty();
        } else if (groupDao.getByName(group.getName()).get().getId() == group.getId()) {
            return true;
        } else {
            String msg = format("Group with name %s already exist", group.getName());
            throw new NotUniqueNameException(msg);
        }
    }
}
