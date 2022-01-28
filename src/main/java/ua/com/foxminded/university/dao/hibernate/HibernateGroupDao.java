package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateGroupDao implements GroupDao {

    private SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Group group) {
        sessionFactory.getCurrentSession().save(group);
    }

    public Optional<Group> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Group.class)
            .loadOptional(id);
    }

    public void update(Group group) {
        sessionFactory.getCurrentSession().update(group);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Group_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Group> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Group_getAll", Group.class)
            .list();
    }

    @Override
    public Optional<Group> getByName(String name) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Group_getByName", Group.class)
            .setParameter("name", name)
            .uniqueResultOptional();
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        List<Group> groups = sessionFactory.getCurrentSession()
            .createNamedQuery("Group_getAll", Group.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Group>(groups, pageable, countTotalRows());
    }

    @Override
    public Long countTotalRows() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Group_countAllRows", Long.class)
            .getSingleResult();
    }
}
