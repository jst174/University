package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Time;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_time_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TimeRepositoryTest {

    @Autowired
    private TimeRepository timeRepository;

    @Test
    public void givenStartAndEndTime_whenGetByTime_thenReturn() {
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(9, 30);
        Time expected = new Time(start, end);

        Optional<Time> actual = timeRepository.findByStartTimeAndEndTime(start, end);

        assertEquals(expected, actual.get());
    }
}
