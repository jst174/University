package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_holiday_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void givenHolidayDate_whenGetByDate_thenReturn() {
        LocalDate date = LocalDate.of(2021, 12, 31);
        Holiday expected = new Holiday("New Year", date);

        Optional<Holiday> actual = holidayRepository.findByDate(LocalDate.of(2021, 12, 31));

        assertEquals(expected, actual.get());
    }
}
