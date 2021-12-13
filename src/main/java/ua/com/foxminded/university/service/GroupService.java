package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;

import java.util.List;

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
        logger.debug("Creating group with name = {}", group.getName());
        verifyNameUniqueness(group);
        groupDao.create(group);

    }

    public Group getById(int id) throws EntityNotFoundException {
        logger.debug("Getting group with id = {}", id);
        return groupDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Group with id = %s not found", id)));
    }

    public void update(Group group) throws NotUniqueNameException {
        logger.debug("Updating group with id = {}", group.getId());
        verifyNameUniqueness(group);
        groupDao.update(group);

    }

    public void delete(int id) {
        logger.debug("Deleting group with id = {}", id);
        groupDao.delete(id);
    }

    public Page<Group> getAll(Pageable pageable) {
        logger.debug("Getting all group");
        return groupDao.getAll(pageable);
    }

    public List<Group> getByLessonId(int lessonId) {
        logger.debug("Getting groups by lesson with id = {}", lessonId);
        return groupDao.getByLessonId(lessonId);
    }

    private void verifyNameUniqueness(Group group) throws NotUniqueNameException {
        if (groupDao.getByName(group.getName())
            .filter(g -> g.getId() != group.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Group with name = %s already exist", group.getName()));
        }
    }
}
