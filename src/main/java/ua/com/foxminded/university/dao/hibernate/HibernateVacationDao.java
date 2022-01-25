package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateVacationDao implements VacationDao {

    private SessionFactory sessionFactory;

    public HibernateVacationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Vacation vacation) {
        sessionFactory.getCurrentSession().save(vacation);
    }

    public Optional<Vacation> getById(int id) {
        try {
            return sessionFactory.getCurrentSession()
                .byId(Vacation.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Vacation vacation) {
        sessionFactory.getCurrentSession().update(vacation);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Vacation WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Vacation> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Vacation")
            .list();
    }

    @Override
    public List<Vacation> getByTeacherId(int id) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Vacation WHERE teacher.id=:id")
            .setParameter("id", id)
            .list();
    }

    @Override
    public Optional<Vacation> getByTeacherAndLessonDate(Teacher teacher, LocalDate lessonDate) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Vacation WHERE teacher.id=:id AND :lessonDate BETWEEN start AND end")
                .setParameter("id", teacher.getId())
                .setParameter("lessonDate", lessonDate)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Vacation> getByTeacherAndVacationDates(Vacation vacation) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Vacation WHERE teacher.id=:id AND start=:start AND end=:end")
                .setParameter("id", vacation.getTeacher().getId())
                .setParameter("start", vacation.getStart())
                .setParameter("end", vacation.getEnd())
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Vacation> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Vacation")
            .uniqueResult();
        List<Vacation> vacations = session
            .createQuery("FROM Vacation")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Vacation>(vacations, pageable, totalRows);
    }
}
