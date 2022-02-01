package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateHolidayDao implements HolidayDao {

    private SessionFactory sessionFactory;

    public HibernateHolidayDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Holiday holiday) {
        sessionFactory.getCurrentSession().save(holiday);
    }

    public Optional<Holiday> getById(int id) {
        return sessionFactory.getCurrentSession().byId(Holiday.class).loadOptional(id);
    }

    public void update(Holiday holiday) {
        sessionFactory.getCurrentSession().update(holiday);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Holiday_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Holiday> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Holiday_getAll", Holiday.class)
            .list();
    }

    @Override
    public Page<Holiday> getAll(Pageable pageable) {
        List<Holiday> holidays = sessionFactory.getCurrentSession()
            .createNamedQuery("Holiday_getAll", Holiday.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Holiday>(holidays, pageable, count());
    }

    @Override
    public Long count() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Holiday_countAllRows", Long.class)
            .getSingleResult();
    }

    @Override
    public Optional<Holiday> getByDate(LocalDate date) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Holiday_getByDate", Holiday.class)
            .setParameter("date", date)
            .uniqueResultOptional();
    }

}
