package ua.com.foxminded.university.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.SpringConfigTest;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_classroom_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcClassroomDaoTest {

    @Autowired
    private JdbcClassroomDao classroomDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewClassroom_whenCreate_thenCreated(){
        Classroom classroom = new Classroom(102, 30);

        classroomDao.create(classroom);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "classrooms"));
    }

    @Test
    public void givenId_whenGetById_thenReturn(){
        Classroom expected = new Classroom(102, 30);

        Classroom actual = classroomDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedClassroomAndId_whenUpdate_thenUpdated(){
        String SQL = "SELECT COUNT(0) FROM classrooms WHERE number = 105 and capacity = 40";
        Classroom updatedClassroom = new Classroom(105, 40);

        classroomDao.update(1, updatedClassroom);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted(){
        classroomDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "classrooms"));
    }

    @Test
    public void whenGetAll_thenReturnAllClassrooms(){
        Classroom classroom1 = new Classroom(203, 60);
        Classroom classroom2 = new Classroom(102, 30);
        List<Classroom> expected = new ArrayList<>();
        expected.add(classroom2);
        expected.add(classroom1);

        List<Classroom> actual = classroomDao.getAll();

        assertEquals(expected, actual);
    }
}
