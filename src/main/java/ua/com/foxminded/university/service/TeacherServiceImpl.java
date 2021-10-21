package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;

    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public void create(Teacher teacher) {
        teacherDao.create(teacher);
    }

    @Override
    public Teacher getById(int id) {
        return teacherDao.getById(id);
    }

    @Override
    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }

    @Override
    public void delete(int id) {
        teacherDao.delete(id);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }
}
