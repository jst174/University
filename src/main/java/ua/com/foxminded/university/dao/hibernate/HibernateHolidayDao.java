package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
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
        try {
            return sessionFactory.getCurrentSession().byId(Holiday.class).loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Holiday holiday) {
        sessionFactory.getCurrentSession().update(holiday);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Holiday WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Holiday> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Holiday")
            .list();
    }

    @Override
    public Page<Holiday> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Holiday")
            .uniqueResult();
        List<Holiday> holidays = session
            .createQuery("FROM Holiday")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Holiday>(holidays, pageable, totalRows);
    }

    @Override
    public Optional<Holiday> getByDate(LocalDate date) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Holiday WHERE date=:date")
                .setParameter("date", date)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

}
