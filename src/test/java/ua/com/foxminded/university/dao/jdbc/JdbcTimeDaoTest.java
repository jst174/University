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
import ua.com.foxminded.university.dao.jdbc.JdbcTimeDao;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_time_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTimeDaoTest {

    @Autowired
    public JdbcTimeDao timeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewTime_whenCreate_thenCreated() {
        Time time = new Time(LocalTime.of(10, 00), LocalTime.of(11, 30));

        timeDao.create(time);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "times"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Time expected = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));

        Time actual = timeDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedTimeAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(*) FROM times WHERE start = '8:15' and ending = '9:45'";
        Time updatedTime = new Time(LocalTime.of(8, 15), LocalTime.of(9, 45));

        timeDao.update(1, updatedTime);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "times", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        timeDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "times"));
    }

    @Test
    public void whenGetAll_thenReturnAllTimes(){
        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(14, 00), LocalTime.of(15, 30));
        List<Time> expected = new ArrayList<>();
        expected.add(time1);
        expected.add(time2);

        List<Time> actual = timeDao.getAll();

        assertEquals(expected, actual);
    }
}
