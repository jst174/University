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
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.exceptions.*;
import ua.com.foxminded.university.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private TeacherDao teacherDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private LessonService lessonService;
    private List<Lesson> lessons;
    private List<Teacher> teachers;
    private List<Vacation> vacations;
    private List<Course> courses;
    private List<Group> groups;
    private List<Holiday> holidays;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        dataSource = new DataSource();
        teachers = getTeachers();
        vacations = getVacations(teachers);
        courses = getCourses();
        groups = getGroups();
        holidays = getHolidays();


        lessons = new ArrayList<>();
        Lesson lesson1 = new Lesson(
            getCourses().get(0),
            getClassrooms().get(0),
            getTeachers().get(0),
            LocalDate.of(2021, 10, 26),
            getTimes().get(0));
        lesson1.setId(1);
        lesson1.setGroups(groups);
        Lesson lesson2 = new Lesson(
            getCourses().get(1),
            getClassrooms().get(1),
            getTeachers().get(0),
            LocalDate.of(2021, 10, 26),
            getTimes().get(1)
        );
        lesson2.setGroups(getGroups());
        lesson2.setId(2);
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    public void givenNewLesson_whenCreate_thenCreated() throws IOException, NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
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
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime())).thenReturn(lessons);

        lessonService.create(lesson);

        verify(lessonDao).create(lesson);
    }

    @Test
    public void givenLessonWhereDateIsHoliday_whenCreate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
        Holiday holiday = new Holiday("holiday", LocalDate.of(2021, 12, 15));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(holiday));

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Date 2021-12-15 is not available due to holiday";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenCreate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 11, 6), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Date 2021-11-06 is not available due to weekend";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyClassroom_whenCreate_thenNotAvailableClassroomExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lessons.get(0)));

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.create(lesson));

        String expectedMessage = "Classroom 101 is already busy at this time";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        List<Course> courses = new ArrayList<>();
        Course course = new Course("History");
        courses.add(course);
        teachers.get(0).setCourses(courses);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s cannot teach Math", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(vacations.get(0)));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s on vacation", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyTeacher_whenCreate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lessons.get(1)));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.create(lesson));

        String expectedMessage = format("Teacher %s %s is already busy at this time", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenCreate_thenNotAvailableClassroomExceptionThrow() throws IOException {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
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
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
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
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 10, 26), getTimes().get(0));
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime()))
            .thenReturn(lessons);

        Exception exception = assertThrows(NotAvailableGroupException.class, () -> lessonService.create(lesson));

        String expectedMessage = "One of the groups already has a lesson at this time";
        verify(lessonDao, never()).create(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void givenLessonId_whenGetById_thenReturn() throws EntityNotFoundException {
        Lesson lesson = lessons.get(0);
        when(lessonDao.getById(lesson.getId())).thenReturn(Optional.of(lesson));

        assertEquals(lesson, lessonService.getById(1));
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
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
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
        lesson.setId(3);
        lessons.add(lesson);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lesson));
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime())).thenReturn(lessons);

        lessonService.update(lesson);

        verify(lessonDao).update(lesson);
    }

    @Test
    public void givenLessonWhereDateIsHoliday_whenUpdate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
        Holiday holiday = new Holiday("holiday", LocalDate.of(2021, 12, 15));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(holiday));

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.update(lesson));

        String expectedException = "Date 2021-12-15 is not available due to holiday";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenUpdate_thenNotAvailableDayExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 11, 6), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotAvailableDayException.class, () -> lessonService.update(lesson));

        String expectedException = "Date 2021-11-06 is not available due to weekend";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyClassroom_whenUpdate_thenNotAvailableClassroomExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lessons.get(0)));

        Exception exception = assertThrows(NotAvailableClassroomException.class, () -> lessonService.update(lesson));

        String expectedException = "Classroom 101 is already busy at this time";
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedException, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        List<Course> courses = new ArrayList<>();
        Course course = new Course("History");
        courses.add(course);
        teachers.get(0).setCourses(courses);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s cannot teach Math", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(vacations.get(0)));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s on vacation", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithBusyTeacher_whenUpdate_thenNotAvailableTeacherExceptionThrow() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lessons.get(1)));

        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lessonService.update(lesson));

        String expectedMessage = format("Teacher %s %s is already busy at this time", teachers.get(0).getFirstName(), teachers.get(0).getLastName());
        verify(lessonDao, never()).update(lesson);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenUpdate_thenNotAvailableClassroomExceptionThrow() throws IOException {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
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
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
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
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 10, 26), getTimes().get(0));
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lesson));
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime()))
            .thenReturn(lessons);

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

    private List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher = dataSource.generateTeacher();
        teacher.setCourses(getCourses());
        teacher.setId(1);
        teachers.add(teacher);
        return teachers;
    }

    private List<Vacation> getVacations(List<Teacher> teachers) {
        Teacher teacher = teachers.get(0);
        List<Vacation> vacations = new ArrayList<>();
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 11, 5),
            LocalDate.of(2021, 11, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 5),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(1);
        vacations.add(vacation1);
        vacations.add(vacation2);
        return vacations;
    }

    private List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("Math");
        course1.setId(1);
        Course course2 = new Course("Physics");
        course2.setId(2);
        courses.add(course1);
        courses.add(course2);
        return courses;
    }

    private List<Classroom> getClassrooms() {
        List<Classroom> classrooms = new ArrayList<>();
        Classroom classroom1 = new Classroom(101, 30);
        classroom1.setId(1);
        Classroom classroom2 = new Classroom(120, 60);
        classroom2.setId(2);
        classrooms.add(classroom1);
        classrooms.add(classroom2);
        return classrooms;
    }

    private List<Time> getTimes() {
        List<Time> times = new ArrayList<>();
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        time1.setId(1);
        Time time2 = new Time(LocalTime.of(12, 0), LocalTime.of(13, 30));
        time2.setId(2);
        times.add(time1);
        times.add(time2);
        return times;
    }

    private List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        Group group1 = new Group("MH-12");
        Group group2 = new Group("LF-43");
        Group group3 = new Group("DF-32");
        groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        return groups;
    }

    private List<Holiday> getHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2022, 1, 1));
        Holiday holiday2 = new Holiday("Christmas", LocalDate.of(2022, 1, 7));
        holidays.add(holiday1);
        holidays.add(holiday2);
        return holidays;
    }

    private void addStudentToGroup(Group group, int numberOfStudent) throws IOException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberOfStudent; i++) {
            students.add(dataSource.generateStudent());
        }
        group.setStudents(students);
    }
}
