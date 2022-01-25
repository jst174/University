package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.model.Course;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateCourseDao implements CourseDao {

    private SessionFactory sessionFactory;

    public HibernateCourseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Course course) {
        sessionFactory.getCurrentSession().save(course);
    }

    public Optional<Course> getById(int id) {
        try {
            return sessionFactory.getCurrentSession().byId(Course.class).loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Course course) {
        sessionFactory.getCurrentSession().update(course);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Course WHERE id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Course> getAll() {
        return sessionFactory
            .getCurrentSession()
            .createQuery("FROM Course")
            .list();
    }

    @Override
    public List<Course> getByTeacherId(int teacherId) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT t.courses FROM Teacher t where t.id=:id")
            .setParameter("id", teacherId)
            .list();
    }

    @Override
    public Optional<Course> getByName(String name) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Course WHERE name=:name")
                .setParameter("name", name)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Course")
            .uniqueResult();
        List<Course> courses = session
            .createQuery("FROM Course")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<>(courses, pageable, totalRows);
    }
}
