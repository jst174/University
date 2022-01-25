package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
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
        try {
            return sessionFactory.getCurrentSession().byId(Classroom.class).loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Classroom classroom) {
        sessionFactory.getCurrentSession().update(classroom);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Classroom WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Classroom> getAll() {
        return sessionFactory.getCurrentSession().
            createQuery("FROM Classroom ")
            .list();
    }

    @Override
    public Page<Classroom> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Classroom")
            .uniqueResult();
        List<Classroom> classrooms = session
            .createQuery("FROM Classroom")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Classroom>(classrooms, pageable, totalRows);
    }

    @Override
    public Optional<Classroom> findByNumber(int number) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Classroom WHERE number=:number")
                .setParameter("number", number)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
