package ua.com.foxminded.university.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.format.annotation.DateTimeFormat;
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

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    public GroupController(GroupService groupService, LessonService lessonService, LessonMapper lessonMapper) {
        this.groupService = groupService;
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        model.addAttribute("groupPage", groupService.getAll(pageable));
        return "groups/all";
    }

    @GetMapping("/{id}")
    public String getById(
        @PathVariable int id, Model model) throws EntityNotFoundException {
        model.addAttribute("group", groupService.getById(id));
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
        @RequestParam(value = "fromDate", required = false)
        @DateTimeFormat(iso = DATE) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false)
        @DateTimeFormat(iso = DATE) LocalDate toDate) {
        List<Lesson> lessons = lessonService.getByGroupIdBetweenDates(id, fromDate, toDate);
        List<LessonDto> lessonsDto = new ArrayList<>();
        for (Lesson lesson : lessons) {
            LessonDto lessonDto = lessonMapper.convertLessonToLessonDto(lesson);
            lessonsDto.add(lessonDto);
        }
        return lessonsDto;
    }
}
