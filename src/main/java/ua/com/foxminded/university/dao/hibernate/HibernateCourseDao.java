package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.model.Course;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateCourseDao implements CourseDao {

    private final EntityManager entityManager;

    public HibernateCourseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Course course) {
        entityManager.unwrap(Session.class).save(course);
    }

    public Optional<Course> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Course.class)
            .loadOptional(id);
    }

    public void update(Course course) {
        entityManager.unwrap(Session.class).update(course);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Course_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Course> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Course_getAll", Course.class)
            .list();
    }

    @Override
    public Optional<Course> getByName(String name) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Course_getByName", Course.class)
            .setParameter("name", name)
            .uniqueResultOptional();
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        List<Course> courses = entityManager.unwrap(Session.class)
            .createNamedQuery("Course_getAll", Course.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<>(courses, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Course_countAllRows", Long.class)
            .getSingleResult();
    }
}
