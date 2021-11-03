package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_lesson_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewLesson_whenCreate_thenCreated() {
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
        Group group1 = new Group("MG-54");
        Group group2 = new Group("DF-23");
        group1.setId(3);
        group2.setId(4);
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        lesson.setGroups(groups);
        int expectedRows = countRowsInTable(jdbcTemplate, "lessons") + 1;
        int expectedGroupRows = countRowsInTable(jdbcTemplate, "lessons_groups") + 2;

        lessonDao.create(lesson);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "lessons"));
        assertEquals(expectedGroupRows, countRowsInTable(jdbcTemplate, "lessons_groups"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        time.setId(1);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);
        Lesson expected = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time
        );
        expected.setGroups(groups1);

        Optional<Lesson> actual = lessonDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedLesson_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM lessons WHERE classroom_id = 2 and course_id = 2 and teacher_id = 2 and " +
            "date = '2021-06-10' and time_id = 2";
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        address.setId(1);
        Course course = new Course("Physics");
        course.setId(2);
        Classroom classroom = new Classroom(201, 60);
        classroom.setId(2);
        Teacher teacher = new Teacher(
            "Bob",
            "King",
            LocalDate.of(1965, 11, 21),
            Gender.MALE,
            address,
            "5345345",
            "king65@gmail.com",
            AcademicDegree.DOCTORAL
        );
        teacher.setId(2);
        Time time = new Time(LocalTime.of(12, 0), LocalTime.of(13, 30));
        time.setId(2);
        Lesson updatedLesson = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 6, 10),
            time
        );
        updatedLesson.setId(1);
        Group group1 = new Group("DF-23");
        Group group2 = new Group("GF-33");
        group1.setId(4);
        group2.setId(5);
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        updatedLesson.setGroups(groups);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "lessons", sql) + 1;
        int expectedGroupRows = countRowsInTable(jdbcTemplate, "lessons_groups") - 1;

        lessonDao.update(updatedLesson);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "lessons", sql));
        assertEquals(expectedGroupRows, countRowsInTable(jdbcTemplate, "lessons_groups"));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "lessons") - 1;

        lessonDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "lessons"));
    }

    @Test
    public void whenGetAll_thenReturnAllLessons() {
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);
        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson1.setGroups(groups1);
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);

        List<Lesson> actual = lessonDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenTeacherId_whenGetByTeacherId_thenReturnLessons() {
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);
        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);
        Course course = new Course("History");
        course.setId(1);
        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);
        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);
        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson1.setGroups(groups1);
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);

        List<Lesson> actual = lessonDao.getByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenClassroomId_whenGetByClassroomId_thenReturnLessons() {
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);

        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);

        Course course = new Course("History");
        course.setId(1);

        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);

        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);


        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        lesson1.setGroups(groups1);


        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);

        List<Lesson> actual = lessonDao.getByClassroomId(1);

        assertEquals(expected, actual);
    }

    @Test
    public  void givenDateAndTime_whenGetByDateAndTime_thenReturn(){
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);

        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);

        Course course = new Course("History");
        course.setId(1);

        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);

        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);


        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        lesson1.setGroups(groups1);


        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);

        List<Lesson> actual = lessonDao.getByDateAndTime(LocalDate.of(2021, 9, 28), time1);

        assertEquals(expected, actual);
    }

    @Test
    public  void givenDateAndTimeAndTeacher_whenGetByDateAndTimeAndTeacher_thenReturn(){
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);

        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);

        Course course = new Course("History");
        course.setId(1);

        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);

        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);


        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        lesson1.setGroups(groups1);


        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);

        List<Lesson> actual = lessonDao.getByDateAndTimeAndTeacher(LocalDate.of(2021, 9, 28), time1, teacher);

        assertEquals(expected, actual);
    }

    @Test
    public  void givenDateAndTimeAndClassroom_whenGetByDateAndTimeAndClassroom_thenReturn(){
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
            "miller77@gmail.com",
            AcademicDegree.MASTER
        );
        teacher.setId(1);

        Time time1 = new Time(LocalTime.of(8, 00), LocalTime.of(9, 30));
        Time time2 = new Time(LocalTime.of(12, 00), LocalTime.of(13, 30));
        time1.setId(1);
        time2.setId(2);

        Course course = new Course("History");
        course.setId(1);

        Classroom classroom = new Classroom(102, 30);
        classroom.setId(1);

        Group group1 = new Group("MH-12");
        group1.setId(1);
        Group group2 = new Group("JW-23");
        group2.setId(2);
        Group group3 = new Group("MG-54");
        group3.setId(3);
        List<Group> groups1 = new ArrayList<>();
        groups1.add(group1);
        groups1.add(group2);
        groups1.add(group3);


        Lesson lesson1 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time1
        );
        lesson1.setGroups(groups1);


        Lesson lesson2 = new Lesson(
            course,
            classroom,
            teacher,
            LocalDate.of(2021, 9, 28),
            time2
        );
        lesson2.setGroups(groups1);
        lesson1.setId(1);
        lesson2.setId(2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);

        List<Lesson> actual = lessonDao.getByDateAndTimeAndClassroom(LocalDate.of(2021, 9, 28), time1, classroom);

        assertEquals(expected, actual);
    }

}
