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
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
    @Mock
    private CourseService courseService;
    @Mock
    private LessonService lessonService;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddTeachersToModelAndShowViewWithAllTeachers() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Teacher> expectedTeachers = Arrays.asList(TestData.teacher1, TestData.teacher2);
        Page<Teacher> teacherPage = new PageImpl<Teacher>(expectedTeachers, pageable, expectedTeachers.size());
        when(teacherService.getAll(pageable)).thenReturn(teacherPage);
        mockMvc.perform(get("/teachers")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/all"))
            .andExpect(model().attribute("teacherPage", teacherPage));
    }

    @Test
    public void whenGetById_thenAddTeacherToModelAndShowViewWithTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(TestData.teacher1);
        mockMvc.perform(get("/teachers/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/show"))
            .andExpect(model().attribute("teacher", TestData.teacher1));
    }

    @Test
    public void whenShowCreatingFrom_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/teachers/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(view().name("teachers/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/teachers")
                .param("firstName", "Mike")
                .param("lastName", "Miller")
                .param("gender", "MALE")
                .param("birthDate", "1994-11-12"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(view().name("redirect:/teachers"));
        verify(teacherService).create(TestData.teacher1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(teacherService.getById(1)).thenReturn(TestData.teacher1);
        mockMvc.perform(get("/teachers/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/edit"))
            .andExpect(model().attribute("teacher", TestData.teacher1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/teachers/{id}", 1)
                .param("firstName", "Mike")
                .param("lastName", "Miller")
                .param("gender", "MALE")
                .param("birthDate", "1994-11-12"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teachers"));
        verify(teacherService).update(TestData.teacher1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teachers"));
        verify(teacherService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Teacher with id = 1 not found";
        when(teacherService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/teachers/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }


    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
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
        Course course1 = new Course.Builder()
            .setName("History")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Math")
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
