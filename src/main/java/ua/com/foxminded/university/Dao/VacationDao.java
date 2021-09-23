package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

@Component
public class VacationDao {

    private static final String SQL_INSERT_VACATION =
        "INSERT INTO vacations (start_vacation, end_vacation) values(?,?)";
    private static final String SQL_FIND_VACATION = "SELECT * FROM vacations WHERE vacation_id = ?";
    private static final String SQL_UPDATE_VACATION =
        "UPDATE vacations SET start_vacation = ?, end_vacation = ? WHERE vacation_id = ?)";
    private static final String SQL_DELETE_VACATION = "DELETE FROM vacations WHERE vacation_id = ?";

    @Autowired
    private VacationMapper vacationMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public VacationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Vacation vacation) {
        jdbcTemplate.update(SQL_INSERT_VACATION, vacation.getStart(), vacation.getEnd());
    }

    public Vacation getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_VACATION, vacationMapper, id);
    }

    public void update(Vacation vacation) {
        jdbcTemplate.update(SQL_UPDATE_VACATION,
            vacation.getStart(),
            vacation.getEnd(),
            vacation.getId());
    }

    public void delete(int id){
        jdbcTemplate.update(SQL_DELETE_VACATION, id);
    }

}
