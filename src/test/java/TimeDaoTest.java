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
import ua.com.foxminded.university.Dao.TimeDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Time;

import javax.sql.DataSource;
import java.time.LocalTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class TimeDaoTest {

    @Autowired
    public TimeDao timeDao;
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
    public void create_shouldInsertTimeIntoDB(){
        Time time = new Time(LocalTime.of(8,00), LocalTime.of(9,30));

        timeDao.create(time);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "time"));
    }

    @Test
    public void getById_shouldReturnTimeFromDB(){
        sqlScript.addScript(new ClassPathResource("create_time_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Time expected = new Time(LocalTime.of(8,00), LocalTime.of(9,30));

        Time actual = timeDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateTime(){
        String SQL = "SELECT COUNT(0) FROM time WHERE start_time = '8:15' and end_time = '9:45'";
        sqlScript.addScript(new ClassPathResource("create_time_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Time updatedTime = new Time(LocalTime.of(8,15), LocalTime.of(9,45));
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "time", SQL));

        timeDao.update(1, updatedTime);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "time", SQL));
    }

    @Test
    public void delete_shouldDeleteTimeFromDB(){
        sqlScript.addScript(new ClassPathResource("create_time_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        timeDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "time"));
    }
}
