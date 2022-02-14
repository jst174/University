package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailableTimeException;
import ua.com.foxminded.university.exceptions.NotUniqueTimeException;
import ua.com.foxminded.university.model.Time;
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
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("time", timeService.getById(id));
        return "times/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Time time) {
        return "times/new";
    }

    @PostMapping
    public String create(@ModelAttribute Time time) throws NotAvailableTimeException, NotUniqueTimeException {
        timeService.create(time);
        return "redirect:/times";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("time", timeService.getById(id));
        return "times/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Time time) throws NotAvailableTimeException, NotUniqueTimeException {
        timeService.update(time);
        return "redirect:/times";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        timeService.delete(id);
        return "redirect:/times";
    }

}
