package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateTimeDao implements TimeDao {

    private final EntityManager entityManager;

    public HibernateTimeDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Time time) {
        entityManager.unwrap(Session.class).save(time);
    }

    public Optional<Time> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Time.class)
            .loadOptional(id);
    }

    public void update(Time time) {
        entityManager.unwrap(Session.class).update(time);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Time_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Time> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Time_getAll", Time.class)
            .list();
    }

    @Override
    public Page<Time> getAll(Pageable pageable) {
        List<Time> times = entityManager.unwrap(Session.class)
            .createNamedQuery("Time_getAll", Time.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Time>(times, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Time_countAllRows", Long.class)
            .getSingleResult();
    }

    @Override
    public Optional<Time> getByTime(LocalTime start, LocalTime end) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Time_getByTime", Time.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .uniqueResultOptional();
    }
}
