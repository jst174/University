package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

@RestController
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

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Classroom classroom) {
        return "classrooms/new";
    }

    @PostMapping
    public String create(@ModelAttribute Classroom classroom) throws NotUniqueNameException {
        classroomService.createClassroom(classroom);
        return "redirect:/classrooms";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("classroom", classroomService.getById(id));
        return "classrooms/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Classroom classroom) throws NotUniqueNameException {
        classroomService.update(classroom);
        return "redirect:/classrooms";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        classroomService.delete(id);
        return "redirect:/classrooms";
    }

}
