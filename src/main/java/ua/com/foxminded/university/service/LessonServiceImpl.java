package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private LessonDao lessonDao;

    public LessonServiceImpl(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Override
    public void create(Lesson lesson) {
        lessonDao.create(lesson);
    }

    @Override
    public Lesson getById(int id) {
        return lessonDao.getById(id);
    }

    @Override
    public void update(Lesson lesson) {
        lessonDao.update(lesson);
    }

    @Override
    public void delete(int id) {
        lessonDao.delete(id);
    }

    @Override
    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }
}
