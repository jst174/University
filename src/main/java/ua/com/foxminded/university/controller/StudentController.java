package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("studentPage", studentService.getAll(pageable));
        return "students/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("student", studentService.getById(id));
        return "students/show";
    }
}