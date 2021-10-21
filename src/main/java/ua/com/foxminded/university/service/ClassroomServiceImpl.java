package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private ClassroomDao classroomDao;

    public ClassroomServiceImpl(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    @Override
    public void create(Classroom classroom) {
        classroomDao.create(classroom);
    }

    @Override
    public Classroom getById(int id) {
        return classroomDao.getById(id);
    }

    @Override
    public void update(Classroom classroom) {
        classroomDao.update(classroom);
    }

    @Override
    public void delete(int id) {
        classroomDao.delete(id);
    }

    @Override
    public List<Classroom> getAll() {
        return classroomDao.getAll();
    }
}
