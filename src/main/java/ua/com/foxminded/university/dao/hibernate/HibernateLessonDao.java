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
        try {
            return sessionFactory.getCurrentSession()
                .byId(Lesson.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Lesson lesson) {
        sessionFactory.getCurrentSession().update(lesson);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Lesson WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Lesson> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Lesson")
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
                .createQuery("FROM Lesson WHERE date=:date AND time.id=:timeId AND teacher.id=:teacherId")
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
        try {
            return sessionFactory.getCurrentSession()
                .createQuery("FROM Lesson WHERE date=:date AND time.id=:timeId AND classroom.id=:classroomId")
                .setParameter("date", date)
                .setParameter("timeId", time.getId())
                .setParameter("classroomId", classroom.getId())
                .uniqueResultOptional();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> getByDateAndTime(LocalDate date, Time time) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Lesson WHERE date=:date AND time.id=:timeId")
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
            .createQuery("SELECT l FROM Lesson l join Group g WHERE g.id=:groupId AND l.date BETWEEN :fromDate AND :toDate")
            .setParameter("groupId", groupId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public List<Lesson> getByTeacherIdBetweenDates(int teacherId, LocalDate fromDate, LocalDate toDate) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Lesson  WHERE teacher.id=:teacherId AND date BETWEEN :fromDate AND :toDate")
            .setParameter("teacherId", teacherId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .list();
    }

    @Override
    public Page<Lesson> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Lesson")
            .uniqueResult();
        List<Lesson> lessons = session
            .createQuery("FROM Lesson")
            .setFirstResult((int)pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Lesson>(lessons, pageable, totalRows);
    }
}
