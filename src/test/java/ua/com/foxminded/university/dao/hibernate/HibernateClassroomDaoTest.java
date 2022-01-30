package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_classroom_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateClassroomDaoTest {

    @Autowired
    private ClassroomDao classroomDao;

    @Test
    public void givenNewClassroom_whenCreate_thenCreated() {
        Classroom classroom = new Classroom(102, 30);

        classroomDao.create(classroom);

        Classroom actual = classroomDao.getById(3).get();
        assertEquals(classroom, actual);
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        Optional<Classroom> actual = classroomDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedClassroomAndId_whenUpdate_thenUpdated() {
        Classroom updatedClassroom = new Classroom(105, 40);
        updatedClassroom.setId(1);

        classroomDao.update(updatedClassroom);

        Classroom actual = classroomDao.getById(1).get();
        assertEquals(updatedClassroom, actual);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        classroomDao.delete(1);

        assertEquals(Optional.empty(), classroomDao.getById(1));
    }

    @Test
    public void whenGetAll_thenReturnAllClassrooms() {
        Classroom classroom1 = new Classroom(203, 60);
        Classroom classroom2 = new Classroom(102, 30);
        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(classroom2);
        classrooms.add(classroom1);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Classroom> classroomPage =
            new PageImpl<Classroom>(classrooms, pageable, classrooms.size());

        assertEquals(classroomPage, classroomDao.getAll(pageable));
    }

    @Test
    public void whenGetAll_thenReturn() {
        Classroom classroom1 = new Classroom(203, 60);
        Classroom classroom2 = new Classroom(102, 30);
        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(classroom2);
        classrooms.add(classroom1);

        assertEquals(classrooms, classroomDao.getAll());
    }

    @Test
    public void givenClassroomNumber_whereGetByNumber_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        Optional<Classroom> actual = classroomDao.findByNumber(102);

        assertEquals(expected, actual.get());
    }

    @Test
    public void whenCountTotalRows_thenReturn(){
        assertEquals(2, classroomDao.countTotalRows());
    }
}
