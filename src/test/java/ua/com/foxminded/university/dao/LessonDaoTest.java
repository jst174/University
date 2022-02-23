package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_lesson_test.sql" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Test
    public void givenDateAndTimeAndTeacher_whenGetByDateAndTimeAndTeacher_thenReturn() {
        Lesson expected = TestData.lesson;

        Optional<Lesson> actual = lessonDao.findByDateAndTimeAndTeacher(LocalDate.of(2021, 9, 28),
            expected.getTime().getId(), expected.getTeacher().getId());

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenDateAndTimeAndClassroom_whenGetByDateAndTimeAndClassroom_thenReturn() {
        Lesson expected = TestData.lesson;

        Optional<Lesson> actual = lessonDao.findByDateAndTimeAndClassroom(LocalDate.of(2021, 9, 28),
            expected.getTime().getId(), expected.getClassroom().getId());

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenGroupIdAndTwoDates_whenGetByGroupIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson,
            new Lesson.Builder().clone(TestData.lesson).setId(2).setTime(TestData.time2).build());

        List<Lesson> actual = lessonDao.findByGroupIdBetweenDates(
            1, LocalDate.of(2021, 9, 25),
            LocalDate.of(2021, 9, 30));

        assertEquals(expected, actual);
    }

    @Test
    public void givenTeacherIdAndTwoDates_whenGetByTeacherIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson,
            new Lesson.Builder().clone(TestData.lesson).setId(2).setTime(TestData.time2).build());

        List<Lesson> actual = lessonDao.findByTeacherIdBetweenDates(
            1, LocalDate.of(2021, 9, 25),
            LocalDate.of(2021, 9, 30));

        assertEquals(expected, actual);
    }

    @Test
    public void givenDateAndTimeAndGroupId_whenGetByDateAndTimeAndGroupId_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson);

        List<Lesson> actual = lessonDao.findByDateAndTimeAndGroupId(TestData.lesson.getDate(), TestData.lesson.getTime(),
            1);

        assertEquals(expected, actual);
    }

    interface TestData {
        Address address = new Address.Builder()
            .setCountry("Russia")
            .setCity("Saint Petersburg")
            .setStreet("Nevsky Prospect")
            .setHouseNumber("15")
            .setApartmentNumber("45")
            .setPostcode("342423")
            .setId(1)
            .build();

        Classroom classroom1 = new Classroom.Builder()
            .setNumber(102)
            .setCapacity(30)
            .setId(1)
            .build();

        Classroom classroom2 = new Classroom.Builder()
            .setNumber(201)
            .setCapacity(60)
            .setId(2)
            .build();

        Course course1 = new Course.Builder()
            .setName("History")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Physics")
            .setId(2)
            .build();

        Time time1 = new Time.Builder()
            .setId(1)
            .setStartTime(LocalTime.of(8, 0))
            .setEndTime(LocalTime.of(9, 30))
            .build();

        Time time2 = new Time.Builder()
            .setId(2)
            .setStartTime(LocalTime.of(12, 0))
            .setEndTime(LocalTime.of(13, 30))
            .build();

        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1977, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller77@gmail.com")
            .setAcademicDegree(AcademicDegree.MASTER)
            .setVacations(new ArrayList<>())
            .setCourses(new ArrayList<>())
            .setId(1)
            .build();

        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Bob")
            .setLastName("King")
            .setBirtDate(LocalDate.of(1965, 11, 21))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5345345")
            .setEmail("king65@gmail.com")
            .setAcademicDegree(AcademicDegree.DOCTORAL)
            .setVacations(new ArrayList<>())
            .setCourses(new ArrayList<>())
            .setId(2)
            .build();

        Group group1 = new Group.Builder()
            .setId(2)
            .setName("MH-12")
            .build();
        Group group2 = new Group.Builder()
            .setId(2)
            .setName("JW-23")
            .build();
        Group group3 = new Group.Builder()
            .setId(1)
            .setName("MG-54")
            .build();
        Group group4 = new Group.Builder()
            .setId(4)
            .setName("DF-23")
            .build();
        Group group5 = new Group.Builder()
            .setId(5)
            .setName("GF-33")
            .build();

        Lesson lesson = new Lesson.Builder()
            .setId(1)
            .setCourse(course1)
            .setClassroom(classroom1)
            .setTeacher(teacher1)
            .setDate(LocalDate.of(2021, 9, 28))
            .setTime(time1)
            .setGroups(Arrays.asList(group1, group2, group3))
            .build();

    }
}
