package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
public class JdbcClassroomDaoTest {

    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewClassroom_whenCreate_thenCreated() {
        Classroom classroom = new Classroom(102, 30);
        int expectedRows = countRowsInTable(jdbcTemplate, "classrooms") + 1;

        classroomDao.create(classroom);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "classrooms"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        Optional<Classroom> actual = classroomDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedClassroomAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM classrooms WHERE number = 105 and capacity = 40";
        Classroom updatedClassroom = new Classroom(105, 40);
        updatedClassroom.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "classrooms", sql) + 1;

        classroomDao.update(updatedClassroom);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "classrooms", sql));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "classrooms") - 1;

        classroomDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "classrooms"));
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
}
