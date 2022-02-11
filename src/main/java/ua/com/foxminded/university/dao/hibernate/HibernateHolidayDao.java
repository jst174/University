package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateHolidayDao implements HolidayDao {

    private final EntityManager entityManager;

    public HibernateHolidayDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Holiday holiday) {
        entityManager.unwrap(Session.class).save(holiday);
    }

    public Optional<Holiday> getById(int id) {
        return entityManager.unwrap(Session.class).byId(Holiday.class).loadOptional(id);
    }

    public void update(Holiday holiday) {
        entityManager.unwrap(Session.class).update(holiday);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Holiday_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Holiday> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Holiday_getAll", Holiday.class)
            .list();
    }

    @Override
    public Page<Holiday> getAll(Pageable pageable) {
        List<Holiday> holidays = entityManager.unwrap(Session.class)
            .createNamedQuery("Holiday_getAll", Holiday.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Holiday>(holidays, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Holiday_countAllRows", Long.class)
            .getSingleResult();
    }

    @Override
    public Optional<Holiday> getByDate(LocalDate date) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Holiday_getByDate", Holiday.class)
            .setParameter("date", date)
            .uniqueResultOptional();
    }

}
