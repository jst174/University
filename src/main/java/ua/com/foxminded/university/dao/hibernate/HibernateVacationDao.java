package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateVacationDao implements VacationDao {

    private final EntityManager entityManager;

    public HibernateVacationDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Vacation vacation) {
        entityManager.unwrap(Session.class).save(vacation);
    }

    public Optional<Vacation> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Vacation.class)
            .loadOptional(id);
    }

    public void update(Vacation vacation) {
        entityManager.unwrap(Session.class).update(vacation);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Vacation_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Vacation> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Vacation_getAll", Vacation.class)
            .list();
    }

    @Override
    public Optional<Vacation> getByTeacherAndDate(Teacher teacher, LocalDate date) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Vacation_getByTeacherAndDate", Vacation.class)
            .setParameter("id", teacher.getId())
            .setParameter("date", date)
            .uniqueResultOptional();
    }

    @Override
    public Optional<Vacation> getByTeacherAndVacationDates(Teacher teacher, LocalDate start, LocalDate end) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Vacation_getByTeacherAndVacationDates", Vacation.class)
            .setParameter("id", teacher.getId())
            .setParameter("start", start)
            .setParameter("ending", end)
            .uniqueResultOptional();
    }

    @Override
    public Page<Vacation> getAll(Pageable pageable) {
        List<Vacation> vacations = entityManager.unwrap(Session.class)
            .createNamedQuery("Vacation_getAll", Vacation.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Vacation>(vacations, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Vacation_countAllRows", Long.class)
            .getSingleResult();
    }
}
