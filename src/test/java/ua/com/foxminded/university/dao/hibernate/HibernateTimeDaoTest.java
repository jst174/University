package ua.com.foxminded.university.dao.hibernate;

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
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Sql({"/create_time_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateTimeDaoTest {

    @Autowired
    private TimeDao timeDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewTime_whenCreate_thenCreated() {
        Time time = new Time(LocalTime.of(10, 0), LocalTime.of(11, 30));

        timeDao.create(time);

        assertEquals(time, hibernateTemplate.get(Time.class, time.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        Time expected = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));

        assertEquals(expected, timeDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedTimeAndId_whenUpdate_thenUpdated() {
        Time updatedTime = new Time(LocalTime.of(8, 15), LocalTime.of(9, 45));
        updatedTime.setId(1);

        timeDao.update(updatedTime);

        assertEquals(updatedTime, hibernateTemplate.get(Time.class, updatedTime.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        assertNotNull(hibernateTemplate.get(Time.class, 1));

        timeDao.delete(1);

        hibernateTemplate.clear();
        assertNull(hibernateTemplate.get(Time.class, 1));
    }

    @Test
    @Transactional
    public void whenGetAll_thenReturnAllTimes() {
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(14, 0), LocalTime.of(15, 30));
        List<Time> expected = new ArrayList<>();
        expected.add(time1);
        expected.add(time2);

        List<Time> actual = timeDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void givenPageable_whenGetAll_thenReturnAllTimes() {
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(14, 0), LocalTime.of(15, 30));
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);
        Pageable pageable = PageRequest.of(0, times.size());
        Page<Time> timePage = new PageImpl<Time>(times, pageable, times.size());

        assertEquals(timePage, timeDao.getAll(pageable));
    }

    @Test
    @Transactional
    public void givenStartAndEndTime_whenGetByTime_thenReturn() {
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(9, 30);
        Time expected = new Time(start, end);

        Optional<Time> actual = timeDao.getByTime(start, end);

        assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    public void whenCount_thenReturn() {
        assertEquals(2, timeDao.count());
    }
}
