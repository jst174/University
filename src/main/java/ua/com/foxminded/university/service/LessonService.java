package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.exceptions.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Service
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    private VacationDao vacationDao;
    private CourseDao courseDao;
    private HolidayDao holidayDao;
    private GroupDao groupDao;

    public LessonService(
        LessonDao lessonDao,
        TeacherDao teacherDao,
        VacationDao vacationDao,
        CourseDao courseDao,
        HolidayDao holidayDao,
        GroupDao groupDao) {
        this.lessonDao = lessonDao;
        this.teacherDao = teacherDao;
        this.vacationDao = vacationDao;
        this.courseDao = courseDao;
        this.holidayDao = holidayDao;
        this.groupDao = groupDao;
    }

    public void create(Lesson lesson) throws NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        logger.debug("Creating lesson");
        checkConditions(lesson);
        lessonDao.create(lesson);
    }

    public Lesson getById(int id) throws EntityNotFoundException {
        logger.debug("Getting lesson with id = {}", id);
        return lessonDao.getById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Lesson with id = %s not found", id)));
    }

    public void update(Lesson lesson) throws NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        logger.debug("Updating lesson with id = {}", lesson.getId());
        checkConditions(lesson);
        lessonDao.update(lesson);
    }

    public void delete(int id) {
        logger.debug("Deleting lesson with id = {}", id);
        lessonDao.delete(id);
    }

    public Page<Lesson> getAll(Pageable pageable) {
        logger.debug("Getting all lessons");
        return lessonDao.getAll(pageable);
    }

    private void checkConditions(Lesson lesson) throws NotAvailableDayException, NotAvailableClassroomException, NotAvailableTeacherException, NotAvailableGroupException {
        verifyHoliday(lesson);
        verifyWeekend(lesson);
        verifyClassroomBusyness(lesson);
        verifyTeacherMatchWithCourse(lesson);
        verifyTeacherVacation(lesson);
        verifyTeacherBusyness(lesson);
        verifyClassroomCapacity(lesson);
        verifyGroupBusyness(lesson);
    }

    private void verifyTeacherVacation(Lesson lesson) throws NotAvailableTeacherException {
        if (vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()).isPresent()) {
            throw new NotAvailableTeacherException(format("Teacher %s %s on vacation",
                lesson.getTeacher().getFirstName(), lesson.getTeacher().getLastName()));
        }
    }

    private void verifyTeacherMatchWithCourse(Lesson lesson) throws NotAvailableTeacherException {
        if (!lesson.getTeacher().getCourses().contains(lesson.getCourse())) {
            throw new NotAvailableTeacherException(format("Teacher %s %s cannot teach %s",
                lesson.getTeacher().getFirstName(), lesson.getTeacher().getLastName(),
                lesson.getCourse().getName()));
        }
    }

    private void verifyTeacherBusyness(Lesson lesson) throws NotAvailableTeacherException {
        if (lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher())
            .filter(l -> l.getId() != lesson.getId())
            .isPresent()) {
            throw new NotAvailableTeacherException(format("Teacher %s %s is already busy at this time",
                lesson.getTeacher().getFirstName(), lesson.getTeacher().getLastName()));
        }
    }

    private void verifyClassroomBusyness(Lesson lesson) throws NotAvailableClassroomException {
        if (lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom())
            .filter(l -> l.getId() != lesson.getId())
            .isPresent()) {
            throw new NotAvailableClassroomException(format("Classroom %s is already busy at this time",
                lesson.getClassroom().getNumber()));
        }
    }

    private void verifyHoliday(Lesson lesson) throws NotAvailableDayException {
        if (holidayDao.getByDate(lesson.getDate()).isPresent()) {
            throw new NotAvailableDayException(format("Date %s is not available due to holiday", lesson.getDate()));
        }
    }

    private void verifyWeekend(Lesson lesson) throws NotAvailableDayException {
        LocalDate lessonDate = lesson.getDate();
        if ((lessonDate.getDayOfWeek() == DayOfWeek.SATURDAY) || (lessonDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new NotAvailableDayException(format("Date %s is not available due to weekend", lessonDate));
        }
    }

    private void verifyClassroomCapacity(Lesson lesson) throws NotAvailableClassroomException {
        List<Group> groups = lesson.getGroups();
        int classroomCapacity = lesson.getClassroom().getCapacity();
        int numberOfStudents = groups.stream().mapToInt(group -> group.getStudents().size()).reduce(Integer::sum).orElse(0);
        if (numberOfStudents > classroomCapacity) {
            throw new NotAvailableClassroomException(format("Classroom %s is not available. " +
                    "Classroom capacity = %s is less than the number of students = %s",
                lesson.getClassroom().getNumber(), classroomCapacity, numberOfStudents));
        }
    }

    private void verifyGroupBusyness(Lesson newLesson) throws NotAvailableGroupException {
        if (lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime())
            .stream().filter(l -> l.getId() != newLesson.getId())
            .noneMatch(l -> l.getGroups().retainAll(newLesson.getGroups()))) {
            throw new NotAvailableGroupException("One of the groups already has a lesson at this time");
        }
    }
}
