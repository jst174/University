package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.service.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("classroomPage", classroomService.getAll(pageable));
        return "classrooms/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("classroom", classroomService.getById(id));
        return "classrooms/show";
    }
}
