package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateStudentDao implements StudentDao {

    private AddressDao addressDao;
    private SessionFactory sessionFactory;

    public HibernateStudentDao(AddressDao addressDao, SessionFactory sessionFactory) {
        this.addressDao = addressDao;
        this.sessionFactory = sessionFactory;
    }

    public void create(Student student) {
        addressDao.create(student.getAddress());
        sessionFactory.getCurrentSession().save(student);
    }

    public Optional<Student> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Student.class)
            .loadOptional(id);
    }

    public void update(Student updatedStudent) {
        sessionFactory.getCurrentSession().update(updatedStudent);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Student_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Student> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Student_getAll", Student.class)
            .list();
    }

    @Override
    public Optional<Student> getByName(String firstName, String lastName) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Student_getByName", Student.class)
            .setParameter("firstName", firstName)
            .setParameter("lastName", lastName)
            .uniqueResultOptional();
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        List<Student> students = sessionFactory.getCurrentSession()
            .createNamedQuery("Student_getAll", Student.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Student>(students, pageable, countTotalRows());
    }

    @Override
    public Long countTotalRows() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Student_countAllRows", Long.class)
            .getSingleResult();
    }
}
