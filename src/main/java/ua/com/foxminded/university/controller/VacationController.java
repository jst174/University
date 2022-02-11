package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotAvailablePeriodException;
import ua.com.foxminded.university.exceptions.NotUniqueVacationDatesException;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

@RestController
@RequestMapping("/vacations")
public class VacationController {

    private final VacationService vacationService;
    private final TeacherService teacherService;

    public VacationController(VacationService vacationService, TeacherService teacherService) {
        this.vacationService = vacationService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("vacationPage", vacationService.getAll(pageable));
        return "vacations/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("vacation", vacationService.getById(id));
        return "vacations/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Vacation vacation, Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "vacations/new";
    }

    @PostMapping
    public String create(@ModelAttribute Vacation vacation) throws NotAvailablePeriodException, NotUniqueVacationDatesException, EntityNotFoundException {
        vacationService.create(vacation);
        return "redirect:/vacations";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("vacation", vacationService.getById(id));
        model.addAttribute("teachers", teacherService.getAll());
        return "vacations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Vacation vacation) throws NotAvailablePeriodException, NotUniqueVacationDatesException, EntityNotFoundException {
        vacationService.update(vacation);
        return "redirect:/vacations";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        vacationService.delete(id);
        return "redirect:/vacations";
    }
}
