package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Service
public class TeacherService {

    private TeacherDao teacherDao;
    private VacationDao vacationDao;

    public TeacherService(TeacherDao teacherDao, VacationDao vacationDao) {
        this.teacherDao = teacherDao;
        this.vacationDao = vacationDao;
    }

    public void create(Teacher teacher) {
        if (isUnique(teacher)) {
            teacherDao.create(teacher);
        }

    }

    public Teacher getById(int id) {
        return teacherDao.getById(id).get();
    }

    public void update(Teacher teacher) {
        if (isCurrent(teacher)) {
            teacherDao.update(teacher);
        }
    }

    public void delete(int id) {
        teacherDao.delete(id);

    }

    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    private boolean isUnique(Teacher teacher) {
        return teacherDao.getByName(teacher.getFirstName(), teacher.getLastName()).isEmpty();
    }

    public boolean isCurrent(Teacher teacher) {
        return teacherDao.getByName(teacher.getFirstName(), teacher.getLastName())
            .get()
            .getId() == teacher.getId();
    }

}
