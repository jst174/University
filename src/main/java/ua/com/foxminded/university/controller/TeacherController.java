package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
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
}
