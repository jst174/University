package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
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
public class HibernateVacationDao implements VacationDao {

    private SessionFactory sessionFactory;

    public HibernateVacationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Vacation vacation) {
        sessionFactory.getCurrentSession().save(vacation);
    }

    public Optional<Vacation> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Vacation.class)
            .loadOptional(id);
    }

    public void update(Vacation vacation) {
        sessionFactory.getCurrentSession().update(vacation);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Vacation_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Vacation> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Vacation_getAll", Vacation.class)
            .list();
    }

    @Override
    public Optional<Vacation> getByTeacherAndDate(Teacher teacher, LocalDate date) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Vacation_getByTeacherAndLessonDate", Vacation.class)
            .setParameter("id", teacher.getId())
            .setParameter("lessonDate", date)
            .uniqueResultOptional();
    }

    @Override
    public Optional<Vacation> getByTeacherAndVacationDates(Teacher teacher, LocalDate start, LocalDate end) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Vacation_getByTeacherAndVacationDates", Vacation.class)
            .setParameter("id", teacher.getId())
            .setParameter("start", start)
            .setParameter("ending", end)
            .uniqueResultOptional();
    }

    @Override
    public Page<Vacation> getAll(Pageable pageable) {
        List<Vacation> vacations = sessionFactory.getCurrentSession()
            .createNamedQuery("Vacation_getAll", Vacation.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Vacation>(vacations, pageable, count());
    }

    @Override
    public Long count() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Vacation_countAllRows", Long.class)
            .getSingleResult();
    }
}
