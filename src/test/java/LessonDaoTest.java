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
import ua.com.foxminded.university.Dao.LessonDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class LessonDaoTest {

    @Autowired
    private LessonDao lessonDao;
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
        sqlScript.addScript(new ClassPathResource("create_lesson_test.sql"));
        DatabasePopulatorUtils.execute(sqlScript, dataSource);
    }

    @Test
    public void create_ShouldInsertLessonIntoDB() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
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
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Lesson lesson = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );

        lessonDao.create(lesson);

        Assertions.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
    }

    @Test
    public void getById_shouldReturnLessonFromDB() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Teacher teacher = new Teacher(
            "Mike",
            "Miller",
            LocalDate.of(1977, 05, 13),
            Gender.MALE,
            address,
            "5435345334",
            "miller97@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Lesson expected = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );

        Lesson actual = lessonDao.getById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void delete_shouldDeleteLessonFromDB() {
        lessonDao.delete(1);

        Assertions.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
    }
}
