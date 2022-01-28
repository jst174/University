package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateClassroomDao implements ClassroomDao {

    private SessionFactory sessionFactory;

    public HibernateClassroomDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Classroom classroom) {
        sessionFactory.getCurrentSession().save(classroom);
    }

    public Optional<Classroom> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Classroom.class)
            .loadOptional(id);
    }

    public void update(Classroom classroom) {
        sessionFactory.getCurrentSession().update(classroom);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Classroom_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Classroom> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Classroom_getAll", Classroom.class)
            .list();
    }

    @Override
    public Page<Classroom> getAll(Pageable pageable) {
        List<Classroom> classrooms = sessionFactory.getCurrentSession()
            .createNamedQuery("Classroom_getAll", Classroom.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Classroom>(classrooms, pageable, countTotalRows());
    }

    @Override
    public Optional<Classroom> findByNumber(int number) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Classroom_getByNumber", Classroom.class)
            .setParameter("number", number)
            .uniqueResultOptional();
    }

    @Override
    public Long countTotalRows() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Classroom_countAllRows", Long.class)
            .getSingleResult();
    }
}
