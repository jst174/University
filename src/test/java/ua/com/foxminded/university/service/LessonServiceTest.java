package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void givenNewLesson_whenCreate_thenCreated() throws IOException {
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
    public void givenLessonWhereDateIsHoliday_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
        Holiday holiday = new Holiday("holiday", LocalDate.of(2021, 12, 15));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(holiday));

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 11, 6), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithBusyClassroom_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));


        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lessons.get(0)));

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        List<Course> courses = new ArrayList<>();
        Course course = new Course("History");
        courses.add(course);
        teachers.get(0).setCourses(courses);

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(vacations.get(0)));

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithBusyTeacher_whenCreate_thenNotCreated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lessons.get(1)));

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenCreate_thenNotCreated() throws IOException {
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

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }

    @Test
    public void givenLessonWithBusyGroup_whenCreate_thenNotCreated() throws IOException {
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

        lessonService.create(lesson);

        verify(lessonDao, never()).create(lesson);
    }


    @Test
    public void givenLessonId_whenGetById_thenReturn() {
        Lesson lesson = lessons.get(0);

        when(lessonDao.getById(lesson.getId())).thenReturn(Optional.of(lesson));

        assertEquals(lesson, lessonService.getById(1));
    }

    @Test
    public void givenUpdatedLesson_whenUpdate_thenUpdated() throws IOException {
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
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime())).thenReturn(lessons);

        lessonService.update(lesson);

        verify(lessonDao).update(lesson);
    }

    @Test
    public void givenLessonWhereDateIsHoliday_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
        Holiday holiday = new Holiday("holiday", LocalDate.of(2021, 12, 15));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.of(holiday));
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWhereDateIsWeekend_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), new Classroom(432, 50), teachers.get(0),
            LocalDate.of(2021, 11, 6), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithBusyClassroom_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));


        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.of(lessons.get(0)));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithTeacherNotMatchedCourse_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));
        List<Course> courses = new ArrayList<>();
        Course course = new Course("History");
        courses.add(course);
        teachers.get(0).setCourses(courses);

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.empty());

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithTeacherOnVacation_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.of(vacations.get(0)));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithBusyTeacher_whenUpdate_thenNotUpdated() {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 11, 9), getTimes().get(0));

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.of(lessons.get(1)));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithClassroomWherePlacesIsNotEnough_whenUpdate_thenNotUpdated() throws IOException {
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
            .thenReturn(Optional.of(lesson), Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithBusyGroup_whenUpdate_thenNotUpdated() throws IOException {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 10, 26), getTimes().get(0));
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);

        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(lesson), Optional.empty());
        when(vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher()))
            .thenReturn(Optional.empty());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime()))
            .thenReturn(lessons);

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonWithOtherLessonDateAndTimeAndClassroom_whenUpdate_thenNotUpdated() {
        Lesson lesson = lessons.get(0);
        Lesson otherLesson = lessons.get(1);
        lesson.setDate(otherLesson.getDate());
        lesson.setTime(otherLesson.getTime());
        lesson.setClassroom(otherLesson.getClassroom());
        
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom()))
            .thenReturn(Optional.of(otherLesson));

        lessonService.update(lesson);

        verify(lessonDao, never()).update(lesson);
    }

    @Test
    public void givenLessonId_whenDelete_thenDeleted() {
        lessonService.delete(1);

        verify(lessonDao).delete(1);
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
