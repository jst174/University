package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Holiday;
import java.time.LocalDate;
import java.util.Optional;

public interface HolidayDao extends Dao<Holiday> {

    Optional<Holiday> getByDate(LocalDate date);

}
