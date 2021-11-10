package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {

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

    public void create(Lesson lesson) {
        if (checkConditions(lesson)) {
            lessonDao.create(lesson);
        }

    }

    public Lesson getById(int id) {
        return lessonDao.getById(id).orElseThrow();
    }

    public void update(Lesson lesson) {
        if (checkConditions(lesson)) {
            lessonDao.update(lesson);
        }
    }

    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    private boolean checkConditions(Lesson lesson) {
        return !isHoliday(lesson) && !isWeekend(lesson) && !isClassroomBusy(lesson)
            && isTeacherMatchedCourse(lesson) && !isTeacherOnVacation(lesson) && !isTeacherBusy(lesson)
            && isEnoughClassroomCapacity(lesson) && !isGroupBusy(lesson);
    }

    private boolean isTeacherOnVacation(Lesson lesson) {
        return vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()).isPresent();
    }

    private boolean isTeacherMatchedCourse(Lesson lesson) {
        return lesson.getTeacher().getCourses().contains(lesson.getCourse());
    }

    private boolean isTeacherBusy(Lesson lesson) {
        if (lessonDao.getById(lesson.getId()).isEmpty()) {
            return lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(),
                lesson.getTeacher()).isPresent();
        } else {
            return !(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(),
                lesson.getTeacher()).get().getId() == lesson.getId());
        }
    }

    private boolean isClassroomBusy(Lesson lesson) {
        if (lessonDao.getById(lesson.getId()).isEmpty()) {
            return lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(),
                lesson.getClassroom()).isPresent();
        } else {
            return !(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(),
                lesson.getClassroom()).get().getId() == lesson.getId());
        }
    }

    private boolean isHoliday(Lesson lesson) {
        return holidayDao.getByDate(lesson.getDate()).isPresent();
    }

    private boolean isWeekend(Lesson lesson) {
        LocalDate lessonDate = lesson.getDate();
        return (lessonDate.getDayOfWeek() == DayOfWeek.SATURDAY) || (lessonDate.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    private boolean isEnoughClassroomCapacity(Lesson lesson) {
        List<Group> groups = lesson.getGroups();
        int numberOfStudents = groups.stream().mapToInt(group -> group.getStudents().size()).reduce(Integer::sum).orElse(0);
        return numberOfStudents <= lesson.getClassroom().getCapacity();
    }

    private boolean isGroupBusy(Lesson newLesson) {
        if (lessonDao.getById(newLesson.getId()).isEmpty()) {
            List<Lesson> lessons = lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime());
            List<Group> groups = new ArrayList<>();
            lessons.forEach(lesson -> groups.addAll(lesson.getGroups()));
            return groups.stream().anyMatch(group -> newLesson.getGroups().contains(group));
        } else {
            return lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime())
                .stream()
                .noneMatch(lesson -> lesson.getId() == newLesson.getId());
        }
    }
}
