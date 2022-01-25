package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
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
        try {
            return sessionFactory.getCurrentSession()
                .byId(Group.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Group group) {
        sessionFactory.getCurrentSession().update(group);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Group WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Group> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Group")
            .list();
    }

    @Override
    public List<Group> getByLessonId(int lessonId) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT l.groups FROM Lesson l WHERE l.id=:id")
            .setParameter("id", lessonId)
            .list();
    }

    @Override
    public Optional<Group> getByName(String name) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Group WHERE name=:name")
                .setParameter("name", name)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Group")
            .uniqueResult();
        List<Group> groups = session
            .createQuery("FROM Group")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Group>(groups, pageable, totalRows);
    }
}
