package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Sql({"/create_lesson_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewLesson_whenCreate_thenCreated() {
        Lesson lesson = new Lesson.Builder().clone(TestData.lesson)
            .setDate(LocalDate.of(2021, 12, 12))
            .setGroups(Arrays.asList(TestData.group3, TestData.group4))
            .build();

        lessonDao.create(lesson);

        assertEquals(lesson, hibernateTemplate.get(Lesson.class, lesson.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        Lesson expected = TestData.lesson;

        assertEquals(expected, lessonDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedLesson_whenUpdate_thenUpdated() {
        Lesson updatedLesson = new Lesson.Builder().clone(TestData.lesson)
            .setTeacher(TestData.teacher2)
            .setTime(TestData.time2)
            .setCourse(TestData.course2)
            .setClassroom(TestData.classroom2)
            .setGroups(Arrays.asList(TestData.group4, TestData.group5))
            .setDate(LocalDate.of(2021, 6, 10))
            .build();

        lessonDao.update(updatedLesson);

        assertEquals(updatedLesson, hibernateTemplate.get(Lesson.class, updatedLesson.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        assertNotNull(hibernateTemplate.get(Lesson.class, 1));

        lessonDao.delete(1);

        hibernateTemplate.clear();
        assertNull(hibernateTemplate.get(Lesson.class, 1));
    }

    @Test
    @Transactional
    public void whenGetAll_thenReturnAllLessons() {
        Lesson lesson1 = TestData.lesson;
        Lesson lesson2 = new Lesson.Builder().clone(lesson1)
            .setId(2)
            .setTime(TestData.time2)
            .build();

        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);

        List<Lesson> actual = lessonDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void givenPageable_whenGetAll_thenReturnAllLessons() {
        Lesson lesson1 = TestData.lesson;
        Lesson lesson2 = new Lesson.Builder().clone(lesson1)
            .setId(2)
            .setTime(TestData.time2)
            .build();
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson1);
        lessons.add(lesson2);
        Pageable pageable = PageRequest.of(0, lessons.size());
        Page<Lesson> lessonPage = new PageImpl<Lesson>(lessons, pageable, lessons.size());

        assertEquals(lessonPage, lessonDao.getAll(pageable));
    }

    @Test
    @Transactional
    public void givenDateAndTimeAndTeacher_whenGetByDateAndTimeAndTeacher_thenReturn() {
        Lesson expected = TestData.lesson;

        Optional<Lesson> actual = lessonDao.getByDateAndTimeAndTeacher(LocalDate.of(2021, 9, 28),
            expected.getTime(), expected.getTeacher());

        assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    public void givenDateAndTimeAndClassroom_whenGetByDateAndTimeAndClassroom_thenReturn() {
        Lesson expected = TestData.lesson;

        Optional<Lesson> actual = lessonDao.getByDateAndTimeAndClassroom(LocalDate.of(2021, 9, 28),
            expected.getTime(), expected.getClassroom());

        assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    public void givenGroupIdAndTwoDates_whenGetByGroupIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson,
            new Lesson.Builder().clone(TestData.lesson).setId(2).setTime(TestData.time2).build());

        List<Lesson> actual = lessonDao.getByGroupIdBetweenDates(
            1, LocalDate.of(2021, 9, 25),
            LocalDate.of(2021, 9, 30));

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void givenTeacherIdAndTwoDates_whenGetByTeacherIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson,
            new Lesson.Builder().clone(TestData.lesson).setId(2).setTime(TestData.time2).build());

        List<Lesson> actual = lessonDao.getByTeacherIdBetweenDates(
            1, LocalDate.of(2021, 9, 25),
            LocalDate.of(2021, 9, 30));

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void givenDateAndTimeAndGroupId_whenGetByDateAndTimeAndGroupId_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson);

        List<Lesson> actual = lessonDao.getByDateAndTimeAndGroupId(TestData.lesson.getDate(), TestData.lesson.getTime(),
            1);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void whenCount_thenReturn() {
        assertEquals(2, lessonDao.count());
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
