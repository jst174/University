package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("coursePage", courseService.getAll(pageable));
        return "courses/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("course", courseService.getById(id));
        return "courses/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Course course) {
        return "courses/new";
    }

    @PostMapping
    public String create(@ModelAttribute Course course) throws NotUniqueNameException {
        courseService.create(course);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("course", courseService.getById(id));
        return "courses/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Course course) throws NotUniqueNameException {
        courseService.update(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
        courseService.delete(id);
        return "redirect:/courses";
    }
}
