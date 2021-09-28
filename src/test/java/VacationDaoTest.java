import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.Dao.VacationDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Vacation;

import javax.sql.DataSource;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class VacationDaoTest {

    @Autowired
    public VacationDao vacationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private static ApplicationContext context;
    private ResourceDatabasePopulator sqlScript;

    @BeforeAll
    public static void init() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @BeforeEach
    public void setUpBeforeClass() {
        sqlScript = new ResourceDatabasePopulator();
        sqlScript.addScript(new ClassPathResource("schema_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
    }

    @Test
    public void create_shouldInsertVacationIntoDB() {
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30));

        vacationDao.create(vacation);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations"));
    }

    @Test
    public void getById_shouldReturnVacationFromDB() {
        sqlScript.addScript(new ClassPathResource("create_vacation_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        Vacation expected = new Vacation(
            LocalDate.of(2021, 10, 15),
            LocalDate.of(2021, 10, 30));

        Vacation actual = vacationDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_ShouldUpdateVacation() {
        String SQL = "SELECT COUNT(0) FROM vacations WHERE start_vacation = '2021-11-15' and end_vacation = '2021-11-30'";
        sqlScript.addScript(new ClassPathResource("create_vacation_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Vacation updatedVacation = new Vacation(
            LocalDate.of(2021, 11, 15),
            LocalDate.of(2021, 11, 30));
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", SQL));

        vacationDao.update(1, updatedVacation);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", SQL));
    }

    @Test
    public void delete_shouldDeleteVacationFromDB() {
        sqlScript.addScript(new ClassPathResource("create_vacation_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        vacationDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations"));
    }
}
