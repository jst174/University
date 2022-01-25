package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueDateException;
import ua.com.foxminded.university.model.Holiday;

import static java.lang.String.format;

@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) throws NotUniqueDateException {
        logger.debug("Creating holiday '{}'", holiday.getName());
        verifyDateUniqueness(holiday);
        holidayDao.create(holiday);

    }

    public Holiday getById(int id) throws EntityNotFoundException {
        logger.debug("Getting holiday with id = {}", id);
        return holidayDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Holiday with id = %s not found", id)));
    }

    public void update(Holiday holiday) throws NotUniqueDateException {
        logger.debug("Updating holiday with id = {}", holiday.getId());
        verifyDateUniqueness(holiday);
        holidayDao.update(holiday);

    }

    public void delete(int id) {
        logger.debug("Deleting holiday with id = {}", id);
        holidayDao.delete(id);
    }

    public Page<Holiday> getAll(Pageable pageable) {
        logger.debug("Getting all holidays");
        return holidayDao.getAll(pageable);
    }

    private void verifyDateUniqueness(Holiday holiday) throws NotUniqueDateException {
        if (holidayDao.getByDate(holiday.getDate())
            .filter(h -> h.getId() != holiday.getId())
            .isPresent()) {
            throw new NotUniqueDateException(format("Holiday with date = %s already exist", holiday.getDate()));
        }
    }
}
