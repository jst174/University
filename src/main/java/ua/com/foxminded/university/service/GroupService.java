package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;

import java.util.Collections;
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

    public List<Group> getAll() {
        logger.debug("Getting all group");
        return groupDao.getAll();
    }

    public List<Group> getByLessonId(int lessonId) {
        logger.debug("Getting groups by lesson with id = {}", lessonId);
        return groupDao.getByLessonId(lessonId);
    }

    public Page<Group> findPaginated(Pageable pageable) {
        List<Group> groups = groupDao.getAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Group> list;
        if (groups.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, groups.size());
            list = groups.subList(startItem, toIndex);
        }
        return new PageImpl<Group>(list, PageRequest.of(currentPage, pageSize), groups.size());
    }

    private void verifyNameUniqueness(Group group) throws NotUniqueNameException {
        if (groupDao.getByName(group.getName())
            .filter(g -> g.getId() != group.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Group with name = %s already exist", group.getName()));
        }
    }
}
