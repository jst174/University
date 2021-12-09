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
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_time_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTimeDaoTest {

    @Autowired
    public TimeDao timeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewTime_whenCreate_thenCreated() {
        Time time = new Time(LocalTime.of(10, 00), LocalTime.of(11, 30));
        int expectedRows = countRowsInTable(jdbcTemplate, "times") + 1;

        timeDao.create(time);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "times"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Time expected = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));

        Optional<Time> actual = timeDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedTimeAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(*) FROM times WHERE start = '8:15' and ending = '9:45'";
        Time updatedTime = new Time(LocalTime.of(8, 15), LocalTime.of(9, 45));
        updatedTime.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "times", sql) + 1;

        timeDao.update(updatedTime);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "times", sql));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "times") - 1;
        timeDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "times"));
    }

    @Test
    public void whenGetAll_thenReturnAllTimes() {
        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(14, 00), LocalTime.of(15, 30));
        List<Time> expected = new ArrayList<>();
        expected.add(time1);
        expected.add(time2);

        List<Time> actual = timeDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturnAllTimes() {
        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(14, 00), LocalTime.of(15, 30));
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);
        Pageable pageable = PageRequest.of(0, times.size());
        Page<Time> timePage = new PageImpl<Time>(times, pageable, times.size());

        assertEquals(timePage, timeDao.getAll(pageable));
    }

    @Test
    public void givenStartAndEndTime_whenGetByTime_thenReturn() {
        LocalTime start = LocalTime.of(8, 00);
        LocalTime end = LocalTime.of(9, 30);
        Time expected = new Time(start, end);

        Optional<Time> actual = timeDao.getByTime(start, end);

        assertEquals(expected, actual.get());
    }
}
