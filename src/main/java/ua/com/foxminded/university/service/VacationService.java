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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VacationService {

    private VacationDao vacationDao;

    public VacationService(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    public void create(Vacation vacation) {
        if (isUnique(vacation) && (isPeriodAcceptable(vacation))) {
            vacationDao.create(vacation);
        }
    }

    public Vacation getById(int id) {
        return vacationDao.getById(id).get();
    }

    public void update(Vacation vacation) {
        if ((vacationDao.getById(vacation.getId()).isPresent()) && isPeriodAcceptable(vacation)) {
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
        return !vacations.contains(vacation);
    }

    private boolean isPeriodAcceptable(Vacation vacation) {
        Map<AcademicDegree, Integer> maxPeriodsVacation = new HashMap<>();
        maxPeriodsVacation.put(AcademicDegree.ASSOCIATE, AcademicDegree.ASSOCIATE.getMaxVacationPeriod());
        maxPeriodsVacation.put(AcademicDegree.BACHELOR, AcademicDegree.BACHELOR.getMaxVacationPeriod());
        maxPeriodsVacation.put(AcademicDegree.MASTER, AcademicDegree.MASTER.getMaxVacationPeriod());
        maxPeriodsVacation.put(AcademicDegree.DOCTORAL, AcademicDegree.DOCTORAL.getMaxVacationPeriod());

        int maxVacationPeriod = maxPeriodsVacation.get(vacation.getTeacher().getAcademicDegree());
        return maxVacationPeriod >= Period.between(vacation.getStart(), vacation.getEnd()).getDays();
    }
}
