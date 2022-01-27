package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateTimeDao implements TimeDao {

    private SessionFactory sessionFactory;

    public HibernateTimeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Time time) {
        sessionFactory.getCurrentSession().save(time);
    }

    public Optional<Time> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Time.class)
            .loadOptional(id);
    }

    public void update(Time time) {
        sessionFactory.getCurrentSession().update(time);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Time_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Time> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Time_getAll", Time.class)
            .list();
    }

    @Override
    public Page<Time> getAll(Pageable pageable) {
        List<Time> times = sessionFactory.getCurrentSession()
            .createNamedQuery("Time_getAll", Time.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Time>(times, pageable, countTotalRows());
    }

    @Override
    public Long countTotalRows() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Time_countAllRows", Long.class)
            .getSingleResult();
    }

    @Override
    public Optional<Time> getByTime(LocalTime start, LocalTime end) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Time_getByTime", Time.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .uniqueResultOptional();
    }
}
