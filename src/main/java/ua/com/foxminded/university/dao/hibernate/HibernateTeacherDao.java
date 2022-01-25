package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateTeacherDao implements TeacherDao {

    private SessionFactory sessionFactory;
    private AddressDao addressDao;

    public HibernateTeacherDao(SessionFactory sessionFactory, AddressDao addressDao) {
        this.sessionFactory = sessionFactory;
        this.addressDao = addressDao;
    }

    public void create(Teacher teacher) {
        addressDao.create(teacher.getAddress());
        sessionFactory.getCurrentSession().save(teacher);
    }

    public Optional<Teacher> getById(int id) {
        try {
            return sessionFactory.getCurrentSession()
                .byId(Teacher.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void update(Teacher updatedTeacher) {
        sessionFactory.getCurrentSession().update(updatedTeacher);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Teacher WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Teacher> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Teacher")
            .list();
    }

    @Override
    public Optional<Teacher> getByName(String firstName, String lastName) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Teacher WHERE firstName=:firstName and lastName=:lastName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Teacher> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session.
            createQuery("SELECT COUNT (*) FROM Teacher")
            .uniqueResult();
        List<Teacher> teachers = session
            .createQuery("FROM Teacher")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Teacher>(teachers, pageable, totalRows);
    }
}
