package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.List;

@Service
public class ClassroomService {

    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    public void createClassroom(Classroom classroom) {
        if (!classroomIsExist(classroom)) {
            classroomDao.create(classroom);
        } else {
            throw new IllegalArgumentException("classroom with this number already exists");
        }
    }

    public Classroom getById(int id) {
        if (idIsExist(id)) {
            return classroomDao.getById(id);
        } else {
            throw new IllegalArgumentException("classroom is not found");
        }
    }

    public void update(Classroom classroom) {
        if (idIsExist(classroom.getId())) {
            classroomDao.update(classroom);
        } else {
            throw new IllegalArgumentException("classroom is not found");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            classroomDao.delete(id);
        } else {
            throw new IllegalArgumentException("classroom is not found");
        }

    }

    public List<Classroom> getAll() {
        return classroomDao.getAll();
    }

    private boolean classroomIsExist(Classroom classroom) {
        List<Classroom> classrooms = classroomDao.getAll();
        return classrooms.stream().anyMatch(classroom::equals);
    }

    private boolean idIsExist(int id) {
        List<Classroom> classrooms = classroomDao.getAll();
        return classrooms.stream().anyMatch(classroom -> classroom.getId() == id);
    }
}
