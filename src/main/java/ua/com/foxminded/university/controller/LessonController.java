package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;
    private final TimeService timeService;
    private final StudentService studentService;


    public LessonController(LessonService lessonService, CourseService courseService, TeacherService teacherService, GroupService groupService, ClassroomService classroomService, TimeService timeService, StudentService studentService) {
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.classroomService = classroomService;
        this.timeService = timeService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("lessonPage", lessonService.getAll(pageable));
        return "lessons/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("lesson", lessonService.getById(id));
        return "lessons/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Lesson lesson, Model model) {
        model.addAttribute("courses", courseService.getAll());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("groups", groupService.getAll());
        model.addAttribute("classrooms", classroomService.getAll());
        model.addAttribute("times", timeService.getAll());
        return "lessons/new";
    }

    @PostMapping
    public String create(@ModelAttribute Lesson lesson) throws ServiceException {
        List<Group> groups = new ArrayList<>();
        for (Group group : lesson.getGroups()) {
            group = groupService.getById(group.getId());
            group.setStudents(studentService.getByGroupId(group.getId()));
            groups.add(group);
        }
        lesson.setClassroom(classroomService.getById(lesson.getClassroom().getId()));
        lesson.setTime(timeService.getById(lesson.getTime().getId()));
        lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
        lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
        lesson.setGroups(groups);
        lessonService.create(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("lesson", lessonService.getById(id));
        model.addAttribute("courses", courseService.getAll());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("groups", groupService.getAll());
        model.addAttribute("classrooms", classroomService.getAll());
        model.addAttribute("times", timeService.getAll());
        return "lessons/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Lesson lesson) throws ServiceException {
        List<Group> groups = new ArrayList<>();
        for (Group group : lesson.getGroups()) {
            group = groupService.getById(group.getId());
            group.setStudents(studentService.getByGroupId(group.getId()));
            groups.add(group);
        }
        lesson.setClassroom(classroomService.getById(lesson.getClassroom().getId()));
        lesson.setTime(timeService.getById(lesson.getTime().getId()));
        lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
        lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
        lesson.setGroups(groups);
        lessonService.update(lesson);
        return "redirect:/lessons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        lessonService.delete(id);
        return "redirect:/lessons";
    }

    @GetMapping("/schedule")
    public String showSchedule() {
        return "lessons/schedule";
    }
}
