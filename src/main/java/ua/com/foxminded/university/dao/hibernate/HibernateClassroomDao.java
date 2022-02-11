package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateClassroomDao implements ClassroomDao {

    private final EntityManager entityManager;

    public HibernateClassroomDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Classroom classroom) {
        entityManager.unwrap(Session.class).save(classroom);
    }

    public Optional<Classroom> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Classroom.class)
            .loadOptional(id);
    }

    public void update(Classroom classroom) {
        entityManager.unwrap(Session.class).update(classroom);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Classroom_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Classroom> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Classroom_getAll", Classroom.class)
            .list();
    }

    @Override
    public Page<Classroom> getAll(Pageable pageable) {
        List<Classroom> classrooms = entityManager.unwrap(Session.class)
            .createNamedQuery("Classroom_getAll", Classroom.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Classroom>(classrooms, pageable, count());
    }

    @Override
    public Optional<Classroom> findByNumber(int number) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Classroom_getByNumber", Classroom.class)
            .setParameter("number", number)
            .uniqueResultOptional();
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Classroom_countAllRows", Long.class)
            .getSingleResult();
    }
}
