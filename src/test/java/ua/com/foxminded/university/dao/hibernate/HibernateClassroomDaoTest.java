package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
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
public class HibernateClassroomDaoTest {

    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewClassroom_whenCreate_thenCreated() {
        Classroom classroom = new Classroom(102, 30);

        classroomDao.create(classroom);

        assertEquals(classroom, hibernateTemplate.get(Classroom.class, classroom.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        assertEquals(expected, classroomDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedClassroomAndId_whenUpdate_thenUpdated() {
        Classroom updatedClassroom = new Classroom(105, 40);
        updatedClassroom.setId(1);

        classroomDao.update(updatedClassroom);

        assertEquals(updatedClassroom, hibernateTemplate.get(Classroom.class, updatedClassroom.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        if (hibernateTemplate.get(Classroom.class, 1) != null) {
            classroomDao.delete(1);
            hibernateTemplate.clear();
        }

        assertNull(hibernateTemplate.get(Classroom.class, 1));
    }

    @Test
    @Transactional
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
    @Transactional
    public void whenGetAll_thenReturn() {
        Classroom classroom1 = new Classroom(203, 60);
        Classroom classroom2 = new Classroom(102, 30);
        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(classroom2);
        classrooms.add(classroom1);

        assertEquals(classrooms, classroomDao.getAll());
    }

    @Test
    @Transactional
    public void givenClassroomNumber_whereGetByNumber_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        Optional<Classroom> actual = classroomDao.findByNumber(102);

        assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    public void whenCount_thenReturn() {
        assertEquals(2, classroomDao.count());
    }
}
