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
        try {
            return sessionFactory.getCurrentSession()
                .byId(Time.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Time time) {
        sessionFactory.getCurrentSession().update(time);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Time WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Time> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Time")
            .list();
    }

    @Override
    public Page<Time> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Time")
            .uniqueResult();
        List<Time> times = session
            .createQuery("FROM Time")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Time>(times, pageable, totalRows);
    }

    @Override
    public Optional<Time> getByTime(LocalTime start, LocalTime end) {
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Time WHERE startTime=:start and endTime=:end")
                .setParameter("start", start)
                .setParameter("end", end)
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
