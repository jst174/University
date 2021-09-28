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
import ua.com.foxminded.university.Dao.HolidayDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Holiday;

import javax.sql.DataSource;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class HolidayDaoTest {

    @Autowired
    private HolidayDao holidayDao;
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
    public void create_shouldInsertHolidayIntoDB() {
        Holiday holiday = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        holidayDao.create(holiday);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays"));
    }

    @Test
    public void getById_shouldReturnHolidayFromDB() {
        sqlScript.addScript(new ClassPathResource("create_holiday_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Holiday expected = new Holiday("New Year", LocalDate.of(2021, 12, 31));

        Holiday actual = holidayDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateHoliday() {
        String SQL = "SELECT COUNT(0) FROM holidays WHERE holiday_name = 'Christmas' and holiday_date = '2022-01-07'";
        sqlScript.addScript(new ClassPathResource("create_holiday_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Holiday updatedHoliday = new Holiday("Christmas", LocalDate.of(2022, 01, 07));
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", SQL));

        holidayDao.update(1, updatedHoliday);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", SQL));
    }

    @Test
    public void delete_shouldDeleteHolidayFromDB(){
        sqlScript.addScript(new ClassPathResource("create_holiday_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        holidayDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays"));
    }
}
