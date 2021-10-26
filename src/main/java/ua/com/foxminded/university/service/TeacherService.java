package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

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
        if (!teacherIsExist(teacher)) {
            teacherDao.create(teacher);
        } else {
            throw new IllegalArgumentException("teacher already exist");
        }

    }

    public Teacher getById(int id) {
        if (idIsExist(id)) {
            return teacherDao.getById(id);
        } else {
            throw new IllegalArgumentException("teacher is not found");
        }
    }

    public void update(Teacher teacher) {
        if (idIsExist(teacher.getId())) {
            teacherDao.update(teacher);
        } else {
            throw new IllegalArgumentException("teacher is not found");
        }
    }

    public void delete(int id) {
        if (idIsExist(id)) {
            teacherDao.delete(id);
        } else {
            throw new IllegalArgumentException("teacher is not found");
        }

    }

    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    private boolean teacherIsExist(Teacher teacher) {
        List<Teacher> teachers = teacherDao.getAll();
        return teachers.stream().anyMatch(teacher::equals);
    }

    private boolean idIsExist(int id) {
        List<Teacher> teachers = teacherDao.getAll();
        return teachers.stream().anyMatch(teacher -> teacher.getId() == id);
    }
}
