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
import ua.com.foxminded.university.Dao.TeacherDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.*;

import javax.sql.DataSource;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class TeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;
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
        sqlScript.addScript(new ClassPathResource("create_teacher_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
    }

    @Test
    public void create_shouldInsertTeacherIntoDB() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        Teacher teacher = new Teacher(
            "Alex",
            "King",
            LocalDate.of(1977, 12, 16),
            Gender.MALE,
            address,
            "36d22366",
            "king97@yandex.ru",
            AcademicDegree.MASTER
        );

        teacherDao.create(teacher, 1);

        Assertions.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void getById_shouldReturnTeacherFromDB() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Teacher expected = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 5, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );

        Teacher actual = teacherDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdateTeacher() {
        String SQL = "SELECT COUNT(0) FROM teachers WHERE first_name = 'Alan' and last_name = 'King' and " +
            "birthday = '1945-12-16' and gender = 'MALE' and address_id = 1 and phone_number = '3622366' " +
            "and email = 'king97@yandex.ru' and academic_degree = 'DOCTORAL'";
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher updatedTeacher = new Teacher(
            "Alan",
            "King",
            LocalDate.of(1945, 12, 16),
            Gender.MALE,
            address,
            "3622366",
            "king97@yandex.ru",
            AcademicDegree.DOCTORAL
        );
        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", SQL));

        teacherDao.update(1, updatedTeacher);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", SQL));
    }

    @Test
    public void delete_shouldDeleteTeacherFromDB() {
        teacherDao.delete(1);

        Assertions.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void addVacation_shouldAddVacationToTeacher() {
        sqlScript.addScript(new ClassPathResource("create_teachers_vacation_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        teacherDao.addVacation(1, 1);
        teacherDao.addVacation(1, 2);
        teacherDao.addVacation(1, 3);

        Assertions.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_vacations"));
    }

    @Test
    public void addCourse_shouldAddCourseToTeacher(){
        sqlScript.addScript(new ClassPathResource("create_teacher_courses_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);

        teacherDao.addCourse(1,1);
        teacherDao.addCourse(1,2);
        teacherDao.addCourse(1,3);

        Assertions.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_courses"));
    }
}
