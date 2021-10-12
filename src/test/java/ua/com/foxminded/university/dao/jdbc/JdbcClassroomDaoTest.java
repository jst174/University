package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_classroom_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcClassroomDaoTest {

    @Autowired
    private JdbcClassroomDao classroomDao;
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

        Classroom actual = classroomDao.getById(1);

        assertEquals(expected, actual);
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
        List<Classroom> expected = new ArrayList<>();
        expected.add(classroom2);
        expected.add(classroom1);

        List<Classroom> actual = classroomDao.getAll();

        assertEquals(expected, actual);
    }
}
