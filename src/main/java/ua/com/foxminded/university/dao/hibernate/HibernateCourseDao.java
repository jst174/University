package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.model.Course;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateCourseDao implements CourseDao {

    private final SessionFactory sessionFactory;

    public HibernateCourseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Course course) {
        sessionFactory.getCurrentSession().save(course);
    }

    public Optional<Course> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Course.class)
            .loadOptional(id);
    }

    public void update(Course course) {
        sessionFactory.getCurrentSession().update(course);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Course_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Course> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Course_getAll", Course.class)
            .list();
    }

    @Override
    public Optional<Course> getByName(String name) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Course_getByName", Course.class)
            .setParameter("name", name)
            .uniqueResultOptional();
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        List<Course> courses = sessionFactory.getCurrentSession()
            .createNamedQuery("Course_getAll", Course.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<>(courses, pageable, count());
    }

    @Override
    public Long count() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Course_countAllRows", Long.class)
            .getSingleResult();
    }
}
