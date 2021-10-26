package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;

@Service
public class VacationService {

    private VacationDao vacationDao;
    private TeacherDao teacherDao;

    public VacationService(VacationDao vacationDao, TeacherDao teacherDao) {
        this.vacationDao = vacationDao;
        this.teacherDao = teacherDao;
    }

    public void create(Vacation vacation) {
        if (!vacationIsExist(vacation)) {
            vacationDao.create(vacation);
        } else {
            throw new IllegalArgumentException("vacation already exist");
        }
    }

    public Vacation getById(int id) {
        if (idIsExist(id)) {
            return vacationDao.getById(id);
        } else {
            throw new IllegalArgumentException("vacation is not found");
        }
    }

    public void update(Vacation vacation) {
        if (idIsExist(vacation.getId())) {
            vacationDao.update(vacation);
        } else {
            throw new IllegalArgumentException("vacation is not found");
        }
    }

    public void delete(int id) {
        if (idIsExist(id)) {
            vacationDao.delete(id);
        } else {
            throw new IllegalArgumentException("vacation is not found");
        }
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        if (teacherIdIsExist(teacherId)) {
            return vacationDao.getByTeacherId(teacherId);
        } else {
            throw new IllegalArgumentException("teacher is not found");
        }
    }

    private boolean vacationIsExist(Vacation vacation) {
        List<Vacation> vacations = vacationDao.getAll();
        return vacations.stream().anyMatch(vacation::equals);
    }

    private boolean idIsExist(int id) {
        List<Vacation> vacations = vacationDao.getAll();
        return vacations.stream().anyMatch(vacation -> vacation.getId() == id);
    }

    private boolean teacherIdIsExist(int id) {
        List<Teacher> teachers = teacherDao.getAll();
        return teachers.stream().anyMatch(teacher -> teacher.getId() == id);
    }
}
