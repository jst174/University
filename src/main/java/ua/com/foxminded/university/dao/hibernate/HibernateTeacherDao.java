package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
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
        sessionFactory.getCurrentSession().save(teacher);
    }

    public Optional<Teacher> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Teacher.class)
            .loadOptional(id);
    }

    public void update(Teacher updatedTeacher) {
        sessionFactory.getCurrentSession().update(updatedTeacher);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Teacher_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Teacher> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Teacher_getAll", Teacher.class)
            .list();
    }

    @Override
    public Optional<Teacher> getByName(String firstName, String lastName) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Teacher_getByName", Teacher.class)
            .setParameter("firstName", firstName)
            .setParameter("lastName", lastName)
            .uniqueResultOptional();
    }

    @Override
    public Page<Teacher> getAll(Pageable pageable) {
        List<Teacher> teachers = sessionFactory.getCurrentSession()
            .createNamedQuery("Teacher_getAll", Teacher.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Teacher>(teachers, pageable, count());
    }

    @Override
    public Long count() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Teacher_countAllRows", Long.class)
            .getSingleResult();
    }
}
