package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueDateException;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("holidayPage", holidayService.getAll(pageable));
        return "holidays/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("holiday", holidayService.getById(id));
        return "holidays/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Holiday holiday) {
        return "holidays/new";
    }

    @PostMapping
    public String create(@ModelAttribute Holiday holiday) throws NotUniqueDateException {
        holidayService.create(holiday);
        return "redirect:/holidays";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("holiday", holidayService.getById(id));
        return "holidays/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Holiday holiday) throws NotUniqueDateException {
        holidayService.update(holiday);
        return "redirect:/holidays";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        holidayService.delete(id);
        return "redirect:/holidays";
    }
}
