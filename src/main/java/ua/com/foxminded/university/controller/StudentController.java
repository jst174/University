package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableGroupException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
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

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Student student, Model model, Pageable pageable) {
        model.addAttribute("groups", groupService.getAll(pageable));
        return "students/new";
    }

    @PostMapping
    public String create(@ModelAttribute Student student) throws NotAvailableGroupException, NotUniqueNameException {
        studentService.create(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model, Pageable pageable) throws EntityNotFoundException {
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("groups", groupService.getAll(pageable));
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Student student) throws NotAvailableGroupException, NotUniqueNameException, EntityNotFoundException {
        studentService.update(student);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        studentService.delete(id);
        return "redirect:/students";
    }

}
