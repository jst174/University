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

    @Transactional
    public void create(Student student) {
        addressDao.create(student.getAddress());
        sessionFactory.getCurrentSession().save(student);
    }

    public Optional<Student> getById(int id) {
        try {
            return sessionFactory.getCurrentSession()
                .byId(Student.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Student updatedStudent) {
        sessionFactory.getCurrentSession().update(updatedStudent);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Student WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Student> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Student")
            .list();
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Student WHERE group.id=:groupId")
            .setParameter("groupId", groupId)
            .list();
    }

    @Override
    public Optional<Student> getByName(String firstName, String lastName) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Student WHERE firstName=:firstName and lastName=:lastName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session.
            createQuery("SELECT COUNT (*) FROM Student")
            .uniqueResult();
        List<Student> students = session
            .createQuery("FROM Student")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Student>(students, pageable, totalRows);
    }
}
