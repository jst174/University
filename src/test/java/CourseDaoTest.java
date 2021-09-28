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
import ua.com.foxminded.university.Dao.CourseDao;
import ua.com.foxminded.university.DataSourse;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Course;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})

public class CourseDaoTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    public void create_shouldInsertCourseIntoDB() {
        Course course = new Course("History");

        courseDao.create(course);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
    }

    @Test
    public void getById_shouldReturnCourseFromDB() {
        sqlScript.addScript(new ClassPathResource("create_course_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Course courseExpected = new Course("History");

        Course courseActual = courseDao.getById(1);

        Assertions.assertEquals(courseExpected, courseActual);
    }

    @Test
    public void update_shouldUpdateCourse() {
        String SQL = "SELECT COUNT(0) FROM courses WHERE course_name = 'Math'";
        sqlScript.addScript(new ClassPathResource("create_course_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
        Course updatedCourse = new Course("Math");
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", SQL));

        courseDao.update(1, updatedCourse);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", SQL));

    }

    @Test
    public void delete_shouldDeleteCourseFromDB(){
        sqlScript.addScript(new ClassPathResource("create_course_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        courseDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
    }
}
