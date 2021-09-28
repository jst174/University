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
import ua.com.foxminded.university.Dao.StudentDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class StudentDaoTest {

    @Autowired
    private StudentDao studentDao;
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
        sqlScript.addScript(new ClassPathResource("create_address_test.sql"));
        sqlScript.addScript(new ClassPathResource("create_groups_test.sql"));
        sqlScript.addScript(new ClassPathResource("create_student_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
    }

    @Test
    public void create_shouldInsertStudentIntoDB() throws IOException {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Student student = new Student(
            "Mike",
            "King",
            LocalDate.of(1997,5,13),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru"
        );

        studentDao.create(student, 1);

        Assertions.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    public void getById_ShouldReturn() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Group group = new Group("MJ-12");
        Student expected = new Student(
            "Mike",
            "Miller",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com"
        );
        expected.setGroup(group);

        Student actual = studentDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateStudent() {
        String SQL = "SELECT COUNT(0) FROM students WHERE first_name = 'Mike' and last_name = 'King' and " +
            "birthday = '1997-05-13' and gender = 'MALE' and address_id = 1 and phone_number = '3622366' and email = 'king97@yandex.ru'" +
            "and group_id = 1";
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Group group = new Group("MJ-12");
        group.setId(1);
        Student updatedStudent = new Student(
            "Mike",
            "King",
            LocalDate.of(1997,5,13),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru"
        );
        updatedStudent.setGroup(group);
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students",SQL));

        studentDao.update(1, updatedStudent);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"students", SQL));

    }

    @Test
    public void delete_shouldDeleteStudentFromDB() {
        studentDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
    }
}
