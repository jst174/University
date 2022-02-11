package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateTeacherDao implements TeacherDao {

    private final EntityManager entityManager;

    public HibernateTeacherDao(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    public void create(Teacher teacher) {
        entityManager.unwrap(Session.class).save(teacher);
    }

    public Optional<Teacher> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Teacher.class)
            .loadOptional(id);
    }

    public void update(Teacher updatedTeacher) {
        entityManager.unwrap(Session.class).update(updatedTeacher);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Teacher_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Teacher> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Teacher_getAll", Teacher.class)
            .list();
    }

    @Override
    public Optional<Teacher> getByFirstNameAndLastName(String firstName, String lastName) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Teacher_getByFirstNameAndLastName", Teacher.class)
            .setParameter("firstName", firstName)
            .setParameter("lastName", lastName)
            .uniqueResultOptional();
    }

    @Override
    public Page<Teacher> getAll(Pageable pageable) {
        List<Teacher> teachers = entityManager.unwrap(Session.class)
            .createNamedQuery("Teacher_getAll", Teacher.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Teacher>(teachers, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Teacher_countAllRows", Long.class)
            .getSingleResult();
    }
}
