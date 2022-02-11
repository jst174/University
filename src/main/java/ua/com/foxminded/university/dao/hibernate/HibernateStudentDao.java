package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateStudentDao implements StudentDao {

    private final EntityManager entityManager;

    public HibernateStudentDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Student student) {
        entityManager.unwrap(Session.class).save(student);
    }

    public Optional<Student> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Student.class)
            .loadOptional(id);
    }

    public void update(Student updatedStudent) {
        entityManager.unwrap(Session.class).update(updatedStudent);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Student_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Student> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Student_getAll", Student.class)
            .list();
    }

    @Override
    public Optional<Student> getByFirstNameAndLastName(String firstName, String lastName) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Student_getByFirstNameAndLastName", Student.class)
            .setParameter("firstName", firstName)
            .setParameter("lastName", lastName)
            .uniqueResultOptional();
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        List<Student> students = entityManager.unwrap(Session.class)
            .createNamedQuery("Student_getAll", Student.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Student>(students, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Student_countAllRows", Long.class)
            .getSingleResult();
    }
}
