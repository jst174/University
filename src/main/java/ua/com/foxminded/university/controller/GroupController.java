package ua.com.foxminded.university.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;

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
    public String getById(@PathVariable int id, Model model) throws EntityNotFoundException {
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
    public JSONArray getLessons(@PathVariable int id){
        List<Lesson> lessons = lessonService.getByGroupId(id);
        JSONArray jsonArray = new JSONArray();
        for (Lesson lesson : lessons) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", lesson.getCourse().getName() + " " + lesson.getTime().getStartTime().toString() + "-" +
                lesson.getTime().getEndTime().toString());
            jsonObject.put("start", lesson.getDate().toString());
            jsonObject.put("end", lesson.getDate().toString());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
