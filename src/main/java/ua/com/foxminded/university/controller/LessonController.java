package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.service.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
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
}
