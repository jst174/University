package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("teacherPage", teacherService.getAll(pageable));
        return "teachers/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) throws EntityNotFoundException {
        model.addAttribute("teacher", teacherService.getById(id));
        return "teachers/show";
    }
}
