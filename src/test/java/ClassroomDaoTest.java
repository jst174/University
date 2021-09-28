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
import ua.com.foxminded.university.Dao.ClassroomDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Classroom;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class ClassroomDaoTest {

    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static ApplicationContext context;
    private static ResourceDatabasePopulator sqlScript;

    @BeforeAll
    public static void init(){
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @BeforeEach
    public void setUpBeforeClasses(){
        sqlScript = new ResourceDatabasePopulator();
        sqlScript.addScript(new ClassPathResource("schema_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
    }

    @Test
    public void create_shouldCreateClassroomsIntoDB(){
        Classroom classroom = new Classroom(102, 30);

        classroomDao.create(classroom);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "classrooms"));
    }

    @Test
    public void getById_shouldReturnClassroomFromDB(){
        sqlScript.addScript(new ClassPathResource("create_classroom_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Classroom expected = new Classroom(102, 30);

        Classroom actual = classroomDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateClassroom(){
        String SQL = "SELECT COUNT(0) FROM classrooms WHERE number = 105 and capacity = 40";
        sqlScript.addScript(new ClassPathResource("create_classroom_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Classroom updatedClassroom = new Classroom(105, 40);
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", SQL));

        classroomDao.update(1, updatedClassroom);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", SQL));
    }

    @Test
    public void delete_shouldDeleteClassroomFromDB(){
        sqlScript.addScript(new ClassPathResource("create_classroom_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        classroomDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "classrooms"));
    }
}
