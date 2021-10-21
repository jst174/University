package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;

@Service
public class VacationServiceImpl implements VacationService {

    private VacationDao vacationDao;

    public VacationServiceImpl(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    @Override
    public void create(Vacation vacation) {
        vacationDao.create(vacation);
    }

    @Override
    public Vacation getById(int id) {
        return vacationDao.getById(id);
    }

    @Override
    public void update(Vacation vacation) {
        vacationDao.update(vacation);
    }

    @Override
    public void delete(int id) {
        vacationDao.delete(id);
    }

    @Override
    public List<Vacation> getAll() {
        return vacationDao.getAll();
    }
}
