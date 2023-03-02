package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayRepository;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueDateException;
import ua.com.foxminded.university.model.Holiday;

import static java.lang.String.format;

@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public void create(Holiday holiday) throws NotUniqueDateException {
        logger.debug("Creating holiday '{}'", holiday.getName());
        verifyDateUniqueness(holiday);
        holidayRepository.save(holiday);

    }

    public Holiday getById(int id) throws EntityNotFoundException {
        logger.debug("Getting holiday with id = {}", id);
        return holidayRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Holiday with id = %s not found", id)));
    }

    public void update(Holiday holiday) throws NotUniqueDateException {
        logger.debug("Updating holiday with id = {}", holiday.getId());
        verifyDateUniqueness(holiday);
        holidayRepository.save(holiday);

    }

    public void delete(int id) {
        logger.debug("Deleting holiday with id = {}", id);
        holidayRepository.deleteById(id);
    }

    public Page<Holiday> getAll(Pageable pageable) {
        logger.debug("Getting all holidays");
        return holidayRepository.findAll(pageable);
    }

    private void verifyDateUniqueness(Holiday holiday) throws NotUniqueDateException {
        if (holidayRepository.findByDate(holiday.getDate())
            .filter(h -> h.getId() != holiday.getId())
            .isPresent()) {
            throw new NotUniqueDateException(format("Holiday with date = %s already exist", holiday.getDate()));
        }
    }
}
