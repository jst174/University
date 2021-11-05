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

import static java.time.temporal.ChronoUnit.DAYS;

//@PropertySource("classpath:application.properties")
@Service
public class VacationService {

    private VacationDao vacationDao;
    @Value("#{${maxPeriodsVacation}}")
    private Map<AcademicDegree, Integer> maxPeriodsVacation;

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
        if ((isCurrent(vacation)) && isPeriodAcceptable(vacation)) {
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
        return vacationDao.getByTeacherAndVacationDates(vacation).isEmpty();
    }

    private boolean isPeriodAcceptable(Vacation vacation) {
        int maxVacationPeriod = maxPeriodsVacation.get(vacation.getTeacher().getAcademicDegree());
        return maxVacationPeriod >= DAYS.between(vacation.getStart(), vacation.getEnd());
    }

    private boolean isCurrent(Vacation vacation){
        return vacationDao.getByTeacherAndVacationDates(vacation).get()
            .getId() == vacation.getId();
    }
}
