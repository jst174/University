package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.service.TimeService;

@Controller
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("times", timeService.getAll());
        return "times/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) throws EntityNotFoundException {
        model.addAttribute("time", timeService.getById(id));
        return "times/show";
    }
}
