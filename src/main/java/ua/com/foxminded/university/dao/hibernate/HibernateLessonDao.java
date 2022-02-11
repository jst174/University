package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.Time;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateLessonDao implements LessonDao {

    private final EntityManager entityManager;

    public HibernateLessonDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Lesson lesson) {
        entityManager.unwrap(Session.class).save(lesson);
    }

    public Optional<Lesson> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Lesson.class)
            .loadOptional(id);
    }

    @Override
    public void update(Lesson lesson) {
        entityManager.unwrap(Session.class).update(lesson);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Lesson_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Lesson> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getAll", Lesson.class)
            .list();
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getByDateAndTimeAndTeacher", Lesson.class)
            .setParameter("date", date)
            .setParameter("timeId", time.getId())
            .setParameter("teacherId", teacher.getId())
            .uniqueResultOptional();
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getByDateAndTimeAndClassroom", Lesson.class)
            .setParameter("date", date)
            .setParameter("timeId", time.getId())
            .setParameter("classroomId", classroom.getId())
            .uniqueResultOptional();
    }

    @Override
    public List<Lesson> getByDateAndTimeAndGroupId(LocalDate date, Time time, int groupId) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getByDateAndTimeAndGroupId", Lesson.class)
            .setParameter("time", time)
            .setParameter("date", date)
            .setParameter("groupId", groupId)
            .list();
    }

    @Override
    public List<Lesson> getByGroupIdBetweenDates(int groupId, LocalDate fromDate, LocalDate toDate) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getByGroupIdBetweenDates", Lesson.class)
            .setParameter("groupId", groupId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public List<Lesson> getByTeacherIdBetweenDates(int teacherId, LocalDate fromDate, LocalDate toDate) {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getByTeacherIdBetweenDates", Lesson.class)
            .setParameter("teacherId", teacherId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public Page<Lesson> getAll(Pageable pageable) {
        List<Lesson> lessons = entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_getAll", Lesson.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Lesson>(lessons, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Lesson_countAllRows", Long.class)
            .getSingleResult();
    }
}
