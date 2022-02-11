package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateGroupDao implements GroupDao {

    private final EntityManager entityManager;

    public HibernateGroupDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Group group) {
        entityManager.unwrap(Session.class).save(group);
    }

    public Optional<Group> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Group.class)
            .loadOptional(id);
    }

    public void update(Group group) {
        entityManager.unwrap(Session.class).update(group);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Group_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Group> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Group_getAll", Group.class)
            .list();
    }

    @Override
    public Optional<Group> getByName(String name) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Group_getByName", Group.class)
            .setParameter("name", name)
            .uniqueResultOptional();
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        List<Group> groups = entityManager.unwrap(Session.class)
            .createNamedQuery("Group_getAll", Group.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Group>(groups, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Group_countAllRows", Long.class)
            .getSingleResult();
    }
}
