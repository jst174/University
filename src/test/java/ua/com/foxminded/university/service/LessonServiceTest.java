package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.exceptions.*;
import ua.com.foxminded.university.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private LessonDao lessonDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private HolidayDao holidayDao;
    @Mock
    private CourseDao courseDao;
    @InjectMocks
    private LessonService lessonService;
    private List<Lesson> lessons;
    private List<Group> groups;

    @BeforeEach
    public void setUp() throws IOException {
        lessons = new ArrayList<>();
        lessons.add(TestData.lesson1);
        lessons.add(TestData.lesson2);
        groups = Arrays.asList(TestData.group1, TestData.group2, TestData.group3);
    }

    @Test
    public void givenNewLesson_whenCreate_thenCreated() throws IOException, NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 12, 15), TestData.time1);
        Group group1 = new Group("GD-32");
        group1.setId(1);
        Group group2 = new Group("GF-65");
        group2.setId(2);
        Group group3 = new Group("BF-36");
        group3.setId(3);
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group1.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group2.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group3.getId())).thenReturn(lessons);

        lessonService.create(lesson);

        verify(lessonDao).create(lesson);

    }

    @Test
    public void givenLessonWhereDateIsHoliday_whenCreate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2022, 1, 1), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(TestData.holiday1));

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Date 2022-01-01 is not available due to holiday";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenCreate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 6), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Date 2021-11-06 is not available due to weekend";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyClassroom_whenCreate_thenNotAvailableClassroomExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(TestData.lesson1));

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Classroom 101 is already busy at this time";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(new Course("History"), TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s cannot teach History", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(TestData.vacation1));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s on vacation", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyTeacher_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(TestData.lesson1));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s is already busy at this time", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenCreate_thenNotAvailableClassroomExceptionThrow() throws IOException {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        Group group1 = new Group("GD-32");
        Group group2 = new Group("GF-65");
        Group group3 = new Group("BF-36");
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 11);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Classroom 101 is not available. " +
            "Classroom capacity = 30 is less than the number of students = 31";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyGroup_whenCreate_thenNotAvailableGroupExceptionThrow() throws IOException {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 10, 26), TestData.time1);
        lesson.setGroups(new ArrayList<>(Arrays.asList(TestData.group1, TestData.group2, TestData.group3)));
        addStudentToGroup(TestData.group1, 10);
        addStudentToGroup(TestData.group2, 10);
        addStudentToGroup(TestData.group3, 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group1.getId())).thenReturn(lessons);

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> lessonService.create(lesson));

        String expectedMessage = "One of the groups [MH-12, LF-43, DF-32] already has a lesson at 2021-10-26 08:00-09:30";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void givenLessonId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(lessonDao.getById(TestData.lesson1.getId())).thenReturn(Optional.of(TestData.lesson1));

        assertEquals(TestData.lesson1, lessonService.getById(1));
    }

    @Test
    public void givenNotExistentLessonId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(lessonDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.getById(20));

        String expectedMessage = "Lesson with id = 20 not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenUpdatedLesson_whenUpdate_thenUpdated() throws IOException, NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        Lesson lesson = TestData.lesson1;
        Group group1 = new Group("GD-32");
        Group group2 = new Group("GF-65");
        Group group3 = new Group("BF-36");
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lesson));
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group1.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group2.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group3.getId())).thenReturn(lessons);

        lessonService.update(lesson);

        verify(lessonDao).update(lesson);
    }

    @Test
    public void givenLessonWhereDateIsHoliday_whenUpdate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson.Builder().clone(TestData.lesson1)
            .setDate(LocalDate.of(2022, 1, 1))
            .build();
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(TestData.holiday1));

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.update(lesson));

        String expectedException = "Date 2022-01-01 is not available due to holiday";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenUpdate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson.Builder().clone(TestData.lesson1)
            .setDate(LocalDate.of(2021, 11, 6))
            .build();
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.update(lesson));

        String expectedException = "Date 2021-11-06 is not available due to weekend";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyClassroom_whenUpdate_thenNotAvailableClassroomExceptionThrow() {
        Lesson lesson = new Lesson(TestData.course1, TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(TestData.lesson1));

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.update(lesson));

        String expectedException = "Classroom 101 is already busy at this time";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(new Course("History"), TestData.classroom1, TestData.teacher1,
            LocalDate.of(2021, 11, 9), TestData.time1);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s cannot teach History", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = TestData.lesson1;
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(TestData.vacation1));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s on vacation", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyTeacher_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = TestData.lesson1;
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(TestData.lesson2));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s is already busy at this time", TestData.teacher1.getFirstName(), TestData.teacher1.getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenUpdate_thenNotAvailableClassroomExceptionThrow() throws IOException {
        Lesson lesson = TestData.lesson1;
        Group group1 = new Group("GD-32");
        Group group2 = new Group("GF-65");
        Group group3 = new Group("BF-36");
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 11);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lesson));

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.update(lesson));

        String expectedMessage = "Classroom 101 is not available. " +
            "Classroom capacity = 30 is less than the number of students = 31";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyGroup_whenUpdate_thenNotAvailableGroupExceptionThrow() throws IOException {
        Lesson lesson = TestData.lesson1;
        addStudentToGroup(TestData.group1, 10);
        addStudentToGroup(TestData.group2, 10);
        addStudentToGroup(TestData.group3, 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lesson));
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group1.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group2.getId())).thenReturn(lessons);
        when(lessonDao.getByDateAndTimeAndGroupId(lesson.getDate(), lesson.getTime(), TestData.group3.getId())).thenReturn(lessons);

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> lessonService.update(lesson));

        String expectedMessage = "One of the groups already has a lesson at this time";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonId_whenDelete_thenDeleted() {
        lessonService.delete(1);

        verify(lessonDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Lesson> lessonPage =
            new PageImpl<Lesson>(lessons, pageable, lessons.size());
        when(lessonDao.getAll(pageable)).thenReturn(lessonPage);

        assertEquals(lessonPage, lessonService.getAll(pageable));
    }

    @Test
    public void givenTeacherIdAndTwoDates_whenGetByTeacherIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson1,
            new Lesson.Builder().clone(TestData.lesson1).setId(2).setDate(LocalDate.of(2021, 9, 26)).build());

        when(lessonDao.getByTeacherIdBetweenDates(1, LocalDate.of(2021, 9, 1),
            LocalDate.of(2021, 10, 30))).thenReturn(expected);

        assertEquals(expected, lessonService.getByTeacherIdBetweenDates(1, LocalDate.of(2021, 9, 1),
            LocalDate.of(2021, 10, 30)));
    }

    @Test
    public void givenGroupIdAndTwoDates_whenGetByTeacherIdBetweenDates_thenReturn() {
        List<Lesson> expected = Arrays.asList(TestData.lesson1,
            new Lesson.Builder().clone(TestData.lesson1).setId(2).setDate(LocalDate.of(2021, 9, 26)).build());

        when(lessonDao.getByGroupIdBetweenDates(1, LocalDate.of(2021, 9, 1),
            LocalDate.of(2021, 10, 30))).thenReturn(expected);

        assertEquals(expected, lessonService.getByGroupIdBetweenDates(1, LocalDate.of(2021, 9, 1),
            LocalDate.of(2021, 10, 30)));
    }

    private void addStudentToGroup(Group group, int numberOfStudent) throws IOException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberOfStudent; i++) {
            students.add(new Student());
        }
        group.setStudents(students);
    }

    interface TestData {

        Holiday holiday1 = new Holiday.Builder()
            .setName("New Year")
            .setDate(LocalDate.of(2022, 1, 1))
            .setId(1)
            .build();
        Holiday holiday2 = new Holiday.Builder()
            .setName("Christmas")
            .setDate(LocalDate.of(2022, 1, 7))
            .setId(2)
            .build();
        Course course1 = new Course.Builder()
            .setName("Math")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Physics")
            .setId(2)
            .build();
        Classroom classroom1 = new Classroom.Builder()
            .setNumber(101)
            .setCapacity(30)
            .setId(1)
            .build();
        Classroom classroom2 = new Classroom.Builder()
            .setNumber(120)
            .setCapacity(60)
            .setId(2)
            .build();
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setCourses(new ArrayList<>(Arrays.asList(course1, course2)))
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
            .build();
        Vacation vacation1 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 11, 5))
            .setEnd(LocalDate.of(2021, 11, 30))
            .setTeacher(teacher1)
            .setId(1)
            .build();
        Vacation vacation2 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 5, 5))
            .setEnd(LocalDate.of(2021, 5, 30))
            .setTeacher(teacher1)
            .setId(2)
            .build();
        Time time1 = new Time.Builder()
            .setStartTime(LocalTime.of(8, 0))
            .setEndTime(LocalTime.of(9, 30))
            .setId(1)
            .build();
        Time time2 = new Time.Builder()
            .setStartTime(LocalTime.of(12, 0))
            .setEndTime(LocalTime.of(13, 30))
            .setId(1)
            .build();
        Group group1 = new Group.Builder()
            .setName("MH-12")
            .setId(1)
            .build();
        Group group2 = new Group.Builder()
            .setName("LF-43")
            .setId(2)
            .build();
        Group group3 = new Group.Builder()
            .setName("DF-32")
            .setId(3)
            .build();
        Lesson lesson1 = new Lesson.Builder()
            .setClassroom(classroom1)
            .setTeacher(teacher1)
            .setDate(LocalDate.of(2021, 10, 26))
            .setTime(time1)
            .setCourse(course1)
            .setGroups(new ArrayList<>(Arrays.asList(group1, group2, group3)))
            .setId(1)
            .build();
        Lesson lesson2 = new Lesson.Builder()
            .setTime(time2)
            .setTeacher(teacher1)
            .setCourse(course2)
            .setGroups(new ArrayList<>(Arrays.asList(group1, group2, group3)))
            .setDate(LocalDate.of(2021, 10, 26))
            .setClassroom(classroom2)
            .setId(2)
            .build();
    }
}
