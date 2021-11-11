package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    public void createClassroom(Classroom classroom) {
        if (isUnique(classroom)) {
            classroomDao.create(classroom);
        }
    }

    public Classroom getById(int id) {
        return classroomDao.getById(id).orElseThrow();
    }

    public void update(Classroom classroom) {
        if (isUnique(classroom)) {
            classroomDao.update(classroom);
        }
    }

    public void delete(int id) {
        classroomDao.delete(id);
    }

    public List<Classroom> getAll() {
        return classroomDao.getAll();
    }

    private boolean isUnique(Classroom classroom) {
        if (classroomDao.getById(classroom.getId()).isEmpty()) {
            return classroomDao.findByNumber(classroom.getNumber()).isEmpty();
        } else {
            return classroomDao.findByNumber(classroom.getNumber()).get().getId() == classroom.getId();
        }
    }
}
