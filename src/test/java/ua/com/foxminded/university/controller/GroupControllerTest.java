package ua.com.foxminded.university.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupControllerTest {

    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @Mock
    private LessonService lessonService;
    @InjectMocks
    private GroupController groupController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddGroupToModelAndShowViewWithAllGroup() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Group> expectedGroups = Arrays.asList(TestData.group1, TestData.group2);
        Page<Group> groupPage = new PageImpl<Group>(expectedGroups, pageable, expectedGroups.size());
        when(groupService.getAll(pageable)).thenReturn(groupPage);
        mockMvc.perform(get("/groups")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/all"))
            .andExpect(model().attribute("groupPage", groupPage));
    }

    @Test
    public void whenGetById_thenAddGroupToModelAndShowViewWithGroup() throws Exception {
        when(groupService.getById(1)).thenReturn(TestData.group1);
        mockMvc.perform(get("/groups/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/show"))
            .andExpect(model().attribute("group", TestData.group1));
    }

    @Test
    public void whenShowCreatingForm_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/groups/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("group"))
            .andExpect(view().name("groups/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/groups")
                .param("name", "GD-12"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("group"))
            .andExpect(view().name("redirect:/groups"));
        verify(groupService).create(TestData.group1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(groupService.getById(1)).thenReturn(TestData.group1);
        mockMvc.perform(get("/groups/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/edit"))
            .andExpect(model().attribute("group", TestData.group1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/groups/{id}", 1)
                .param("name", "GD-12"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/groups"));
        verify(groupService).update(TestData.group1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/groups"));
        verify(groupService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Group with id = 1 not found";
        when(groupService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/groups/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }


    interface TestData {
        Group group1 = new Group.Builder()
            .setName("GD-12")
            .setId(1)
            .build();
        Group group2 = new Group.Builder()
            .setName("GS-14")
            .setId(2)
            .build();
        Course course1 = new Course.Builder()
            .setName("History")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Math")
            .setId(2)
            .build();
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setCourses(Arrays.asList(course1, course2))
            .setAcademicDegree(AcademicDegree.MASTER)
            .setEmail("miller@gmail.com")
            .setPhoneNumber("3934234")
            .setId(1)
            .build();
        Time time1 = new Time.Builder()
            .setStartTime(LocalTime.of(8, 0))
            .setEndTime(LocalTime.of(9, 30))
            .setId(1)
            .build();
        Time time2 = new Time.Builder()
            .setStartTime(LocalTime.of(12, 0))
            .setEndTime(LocalTime.of(13, 30))
            .setId(2)
            .build();
        Classroom classroom1 = new Classroom.Builder()
            .setNumber(101)
            .setCapacity(30)
            .setId(1)
            .build();
        Classroom classroom2 = new Classroom.Builder()
            .setNumber(202)
            .setCapacity(50)
            .setId(2)
            .build();
        Lesson lesson1 = new Lesson.Builder()
            .setDate(LocalDate.of(2021, 12, 12))
            .setTime(time1)
            .setClassroom(classroom1)
            .setCourse(course1)
            .setTeacher(teacher1)
            .setGroups(new ArrayList<>(Arrays.asList(LessonControllerTest.TestData.group1, LessonControllerTest.TestData.group2)))
            .setId(1)
            .build();
        Lesson lesson2 = new Lesson.Builder()
            .setDate(LocalDate.of(2021, 12, 13))
            .setTime(time2)
            .setClassroom(classroom2)
            .setTeacher(teacher1)
            .setCourse(course2)
            .setGroups(new ArrayList<>(Arrays.asList(LessonControllerTest.TestData.group1, LessonControllerTest.TestData.group2)))
            .setId(2)
            .build();
    }
}
