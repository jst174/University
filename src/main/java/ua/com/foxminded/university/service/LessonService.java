package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.exceptions.*;
import ua.com.foxminded.university.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Service
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    private LessonRepository lessonRepository;
    private VacationRepository vacationRepository;
    private HolidayRepository holidayRepository;

    public LessonService(
        LessonRepository lessonRepository,
        VacationRepository vacationRepository,
        HolidayRepository holidayRepository) {
        this.lessonRepository = lessonRepository;
        this.vacationRepository = vacationRepository;
        this.holidayRepository = holidayRepository;
    }

    public void create(Lesson lesson) throws NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        logger.debug("Creating lesson");
        checkConditions(lesson);
        lessonRepository.save(lesson);
    }

    public Lesson getById(int id) throws EntityNotFoundException {
        logger.debug("Getting lesson with id = {}", id);
        return lessonRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(format("Lesson with id = %s not found", id)));
    }

    public void update(Lesson lesson) throws NotAvailableTeacherException, NotAvailableGroupException, NotAvailableClassroomException, NotAvailableDayException {
        logger.debug("Updating lesson with id = {}", lesson.getId());
        checkConditions(lesson);
        lessonRepository.save(lesson);
    }

    public void delete(int id) {
        logger.debug("Deleting lesson with id = {}", id);
        lessonRepository.deleteById(id);
    }

    public Page<Lesson> getAll(Pageable pageable) {
        logger.debug("Getting all lessons");
        return lessonRepository.findAll(pageable);
    }

    public List<Lesson> getAll() {
        logger.debug("Getting all lessons");
        return lessonRepository.findAll();
    }

    public List<Lesson> getByGroupIdBetweenDates(int groupId, LocalDate fromDate, LocalDate toDate) {
        logger.debug("Getting lesson where group_id = {} and date1 = {}, date2 = {}", groupId, fromDate, toDate);
        return lessonRepository.findByGroupIdBetweenDates(groupId, fromDate, toDate);
    }

    public List<Lesson> getByTeacherIdBetweenDates(int teacherId, LocalDate fromDate, LocalDate toDate) {
        logger.debug("Getting lesson where teacher_id = {} and date1 = {}, date2 = {}", teacherId, fromDate, toDate);
        return lessonRepository.findByTeacherIdBetweenDates(teacherId, fromDate, toDate);
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
        if (vacationRepository.findByTeacherAndDate(lesson.getTeacher().getId(), lesson.getDate()).isPresent()) {
            throw new NotAvailableTeacherException(format("Teacher %s %s on vacation",
                lesson.getTeacher().getFirstName(), lesson.getTeacher().getLastName()));
        }
    }

    private void verifyTeacherMatchWithCourse(Lesson lesson) throws NotAvailableTeacherException {
        Teacher teacher = lesson.getTeacher();
        List<Course> teacherCourses = lesson.getTeacher().getCourses();
        teacher.setCourses(teacherCourses);
        if (!teacher.getCourses().contains(lesson.getCourse())) {
            throw new NotAvailableTeacherException(format("Teacher %s %s cannot teach %s",
                teacher.getFirstName(), teacher.getLastName(),
                lesson.getCourse().getName()));
        }
    }

    private void verifyTeacherBusyness(Lesson lesson) throws NotAvailableTeacherException {
        if (lessonRepository.findByDateAndTimeIdAndTeacherId(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId())
            .filter(l -> l.getId() != lesson.getId())
            .isPresent()) {
            throw new NotAvailableTeacherException(format("Teacher %s %s is already busy at this time",
                lesson.getTeacher().getFirstName(), lesson.getTeacher().getLastName()));
        }
    }

    private void verifyClassroomBusyness(Lesson lesson) throws NotAvailableClassroomException {
        if (lessonRepository.findByDateAndTimeIdAndClassroomId(lesson.getDate(),
                lesson.getTime().getId(), lesson.getClassroom().getId())
            .filter(l -> l.getId() != lesson.getId())
            .isPresent()) {
            throw new NotAvailableClassroomException(format("Classroom %s is already busy at this time",
                lesson.getClassroom().getNumber()));
        }
    }

    private void verifyHoliday(Lesson lesson) throws NotAvailableDayException {
        if (holidayRepository.findByDate(lesson.getDate()).isPresent()) {
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
        int classroomCapacity = lesson.getClassroom().getCapacity();
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).reduce(Integer::sum).orElse(0);
        if (numberOfStudents > classroomCapacity) {
            throw new NotAvailableClassroomException(format("Classroom %s is not available. " +
                    "Classroom capacity = %s is less than the number of students = %s",
                lesson.getClassroom().getNumber(), classroomCapacity, numberOfStudents));
        }
    }

    private void verifyGroupBusyness(Lesson lesson) throws NotAvailableGroupException {
        if (lesson.getGroups().stream()
            .map(group -> lessonRepository.findByDateAndTimeAndGroupId(lesson.getDate(),
                lesson.getTime(), group.getId()))
            .flatMap(List::stream)
            .distinct()
            .filter(l -> l.getId() != lesson.getId())
            .map(Lesson::getGroups)
            .anyMatch(groups -> groups.stream()
                .anyMatch(lesson.getGroups()::contains))
        ) {
            throw new NotAvailableGroupException(format("One of the groups %s already has a lesson at %s %s",
                lesson.getGroups(), lesson.getDate(), lesson.getTime().toString()));
        }
    }
}
