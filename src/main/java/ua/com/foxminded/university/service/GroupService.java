package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.GroupRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;

import java.util.List;

import static java.lang.String.format;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void create(Group group) throws NotUniqueNameException {
        logger.debug("Creating group with name = {}", group.getName());
        verifyNameUniqueness(group);
        groupRepository.save(group);

    }

    public Group getById(int id) throws EntityNotFoundException {
        logger.debug("Getting group with id = {}", id);
        return groupRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Group with id = %s not found", id)));
    }

    public void update(Group group) throws NotUniqueNameException {
        logger.debug("Updating group with id = {}", group.getId());
        verifyNameUniqueness(group);
        groupRepository.save(group);

    }

    public void delete(int id) {
        logger.debug("Deleting group with id = {}", id);
        groupRepository.deleteById(id);
    }

    public Page<Group> getAll(Pageable pageable) {
        logger.debug("Getting all group");
        return groupRepository.findAll(pageable);
    }

    public List<Group> getAll() {
        logger.debug("Getting all group");
        return groupRepository.findAll();
    }

    private void verifyNameUniqueness(Group group) throws NotUniqueNameException {
        if (groupRepository.findByName(group.getName())
            .filter(g -> g.getId() != group.getId())
            .isPresent()) {
            throw new NotUniqueNameException(format("Group with name = %s already exist", group.getName()));
        }
    }
}
