package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.LessonDto;
import ua.com.foxminded.university.dto.LessonMapper;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    public TeacherController(TeacherService teacherService, CourseService courseService, LessonService lessonService, LessonMapper lessonMapper) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("teacherPage", teacherService.getAll(pageable));
        return "teachers/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("teacher", teacherService.getById(id));
        return "teachers/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Teacher teacher, Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "teachers/new";
    }

    @PostMapping
    public String create(@ModelAttribute Teacher teacher) throws NotUniqueNameException {
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("teacher", teacherService.getById(id));
        model.addAttribute("courses", courseService.getAll());
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Teacher teacher) throws NotUniqueNameException, EntityNotFoundException {
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/getLessons")
    @ResponseBody
    public List<LessonDto> getLessons(
        @PathVariable int id,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE) LocalDate fromDate,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE) LocalDate toDate) {
        return lessonService.getByTeacherIdBetweenDates(id, fromDate, toDate)
            .stream()
            .map(lessonMapper::convertLessonToLessonDto)
            .collect(Collectors.toList());
    }
}
