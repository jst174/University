package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.service.HolidayService;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("holidays", holidayService.getAll());
        return "holidays/all";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) throws EntityNotFoundException {
        model.addAttribute("holiday", holidayService.getById(id));
        return "holidays/show";
    }
}
