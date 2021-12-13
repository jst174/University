package ua.com.foxminded.university.controller;

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
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
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
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Teacher with id = 1 not found";
        when(teacherService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/teachers/1"))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setId(1)
            .build();
    }
}
