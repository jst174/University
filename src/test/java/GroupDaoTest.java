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
import ua.com.foxminded.university.Dao.GroupDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Group;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GroupDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GroupDao groupDao;

    private static ApplicationContext context;
    private  ResourceDatabasePopulator sqlScript;

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
    public void create_shouldInsertGroupIntoDB() {
        Group group = new Group("MJ-12");

        groupDao.create(group);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));

    }

    @Test
    public void getById_shouldReturnGroupFromDB() {
        sqlScript.addScript(new ClassPathResource("create_groups_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        Group group = new Group("MJ-12");

        Assertions.assertEquals(group, groupDao.getById(1));
    }

    @Test
    public void update_shouldUpdateGroup() {
        String SQL = "SELECT COUNT(0) FROM groups WHERE group_name = 'JD-32'";
        sqlScript.addScript(new ClassPathResource("create_groups_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Group updatedGroup = new Group("JD-32");
        int beforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", SQL);
        Assertions.assertEquals(0, beforeUpdate);

        groupDao.update(1, updatedGroup);

        int afterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", SQL);
        Assertions.assertEquals(1, afterUpdate);
    }

    @Test
    public void delete_shouldDeleteGroupFromDB() {
        sqlScript.addScript(new ClassPathResource("create_groups_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        groupDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));
    }

    @Test
    public void addStudent_shouldAddStudentToGroup(){
        sqlScript.addScript(new ClassPathResource("create_groups_test.sql"));
        sqlScript.addScript(new ClassPathResource("create_students_for_group_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        groupDao.addStudent(1,1);
        groupDao.addStudent(1,2);

        Assertions.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "group_students"));

    }


}
