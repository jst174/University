package ua.com.foxminded.university.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.LessonDto;
import ua.com.foxminded.university.dto.LessonMapper;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final LessonService lessonService;

    public GroupController(GroupService groupService, LessonService lessonService) {
        this.groupService = groupService;
        this.lessonService = lessonService;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("groupPage", groupService.getAll(pageable));
        return "groups/all";
    }

    @GetMapping("/{id}")
    public String getById(
        @PathVariable int id, Model model,
        @RequestParam(value = "date1", required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
        @RequestParam(value = "date2", required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2) throws EntityNotFoundException {
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("date1", date1.toString());
        model.addAttribute("date2", date2.toString());
        return "groups/show";
    }

    @GetMapping("/new")
    public String showCreationForm(@ModelAttribute Group group) {
        return "groups/new";
    }

    @PostMapping
    public String create(@ModelAttribute Group group) throws NotUniqueNameException {
        groupService.create(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("group", groupService.getById(id));
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Group group) throws NotUniqueNameException {
        groupService.update(group);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/getLessons")
    @ResponseBody
    public List<LessonDto> getLessons(
        @PathVariable int id,
        @RequestParam(value = "date1", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
        @RequestParam(value = "date2", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2) {
        List<Lesson> lessons = lessonService.getByGroupIdBetweenDates(id, date1, date2);
        return Mappers.getMapper(LessonMapper.class).convertToDtoList(lessons);
    }
}
