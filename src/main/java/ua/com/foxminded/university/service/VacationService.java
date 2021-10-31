package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.time.Period;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class VacationService {

    private VacationDao vacationDao;
    private TeacherDao teacherDao;
    @Value("${vacation.max.period.associate}")
    private int vacationPeriodForAssociate;
    @Value("${vacation.max.period.bachelor}")
    private int vacationPeriodForBachelor;
    @Value("${vacation.max.period.master}")
    private int vacationPeriodForMaster;
    @Value("${vacation.max.period.doctoral}")
    private int vacationPeriodForDoctoral;

    public VacationService(VacationDao vacationDao, TeacherDao teacherDao) {
        this.vacationDao = vacationDao;
        this.teacherDao = teacherDao;
    }

    public void create(Vacation vacation) {
        if (!isUnique(vacation) && (isPeriodAcceptable(vacation))) {
            vacationDao.create(vacation);
        }
    }

    public Vacation getById(int id) {
        return vacationDao.getById(id);
    }

    public void update(Vacation vacation) {
        if ((vacationDao.getById(vacation.getId()).equals(vacation)) && isPeriodAcceptable(vacation)) {
            vacationDao.update(vacation);
        }
    }

    public void delete(int id) {
        vacationDao.delete(id);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        return vacationDao.getByTeacherId(teacherId);
    }

    private boolean isUnique(Vacation vacation) {
        List<Vacation> vacations = vacationDao.getAll();
        return vacations.contains(vacation);
    }

    private boolean isPeriodAcceptable(Vacation vacation) {
        int maxVacationPeriod = 0;
        AcademicDegree academicDegree = vacation.getTeacher().getAcademicDegree();
        if (academicDegree.equals(AcademicDegree.ASSOCIATE)) {
            maxVacationPeriod = vacationPeriodForAssociate;
        } else if (academicDegree.equals(AcademicDegree.BACHELOR)) {
            maxVacationPeriod = vacationPeriodForBachelor;
        } else if (academicDegree.equals(AcademicDegree.MASTER)) {
            maxVacationPeriod = vacationPeriodForMaster;
        } else if (academicDegree.equals(AcademicDegree.DOCTORAL)) {
            maxVacationPeriod = vacationPeriodForDoctoral;
        }
        return maxVacationPeriod >= Period.between(vacation.getStart(), vacation.getEnd()).getDays();
    }
}
