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
import ua.com.foxminded.university.Dao.AddressDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Address;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class AddressDaoTest {

    @Autowired
    private AddressDao addressDao;
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
    public void create_shouldInsertAddressIntoDatabase() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        addressDao.create(address);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "addresses"));
    }

    @Test
    public void getById_shouldReturnAddressFromDB() {
        sqlScript.addScript(new ClassPathResource("create_address_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        Address expected = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        Address actual = addressDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateAddress() {
        String SQL = "SELECT COUNT(0) FROM addresses WHERE country = 'Russia' and city = 'Moscow'" +
            "and street = 'Kutuzov Avenue' and house_number = '43' " +
            "and apartment_number = '192' and postcode = '432436'";
        sqlScript.addScript(new ClassPathResource("create_address_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Address updatedAddress = new Address("Russia", "Moscow", "Kutuzov Avenue",
            "43", "192", "432436");
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "addresses", SQL));

        addressDao.update(1, updatedAddress);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "addresses", SQL));
    }

    @Test
    public void delete_shouldDeleteAddressFromDB(){
        sqlScript.addScript(new ClassPathResource("create_address_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        addressDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "addresses"));
    }
}
