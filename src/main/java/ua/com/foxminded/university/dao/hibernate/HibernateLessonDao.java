package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateLessonDao implements LessonDao {

    private SessionFactory sessionFactory;

    public HibernateLessonDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Lesson lesson) {
        sessionFactory.getCurrentSession().save(lesson);
    }

    public Optional<Lesson> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Lesson.class)
            .loadOptional(id);
    }

    @Override
    public void update(Lesson lesson) {
        sessionFactory.getCurrentSession().update(lesson);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Lesson_delete")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Lesson> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getAll", Lesson.class)
            .list();
    }

    @Override
    public List<Lesson> getByTeacherId(int teacherId) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Lesson WHERE teacher.id=:teacherId")
            .setParameter("teacherId", teacherId)
            .list();
    }

    @Override
    public List<Lesson> getByClassroomId(int classroomId) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Lesson WHERE classroom.id=:classroomId")
            .setParameter("classroomId", classroomId)
            .list();
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndTeacher(LocalDate date, Time time, Teacher teacher) {
        try {
            return sessionFactory.getCurrentSession()
                .createNamedQuery("Lesson_getByDateAndTimeAndTeacher", Lesson.class)
                .setParameter("date", date)
                .setParameter("timeId", time.getId())
                .setParameter("teacherId", teacher.getId())
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lesson> getByDateAndTimeAndClassroom(LocalDate date, Time time, Classroom classroom) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getByDateAndTimeAndClassroom", Lesson.class)
            .setParameter("date", date)
            .setParameter("timeId", time.getId())
            .setParameter("classroomId", classroom.getId())
            .uniqueResultOptional();
    }

    @Override
    public List<Lesson> getByDateAndTime(LocalDate date, Time time) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getByDateAndTime", Lesson.class)
            .setParameter("date", date)
            .setParameter("timeId", time.getId())
            .list();
    }

    @Override
    public List<Lesson> getByGroupId(int groupId) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT l FROM Lesson l join Group g WHERE g.id =:groupId")
            .setParameter("groupId", groupId)
            .list();
    }

    @Override
    public List<Lesson> getByGroupIdBetweenDates(int groupId, LocalDate fromDate, LocalDate toDate) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getByGroupIdBetweenDates", Lesson.class)
            .setParameter("groupId", groupId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public List<Lesson> getByTeacherIdBetweenDates(int teacherId, LocalDate fromDate, LocalDate toDate) {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getByTeacherIdBetweenDates", Lesson.class)
            .setParameter("teacherId", teacherId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public Page<Lesson> getAll(Pageable pageable) {
        List<Lesson> lessons = sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_getAll", Lesson.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Lesson>(lessons, pageable, countTotalRows());
    }

    @Override
    public Long countTotalRows() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Lesson_countAllRows", Long.class)
            .getSingleResult();
    }
}
