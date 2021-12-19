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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private StudentController studentController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddStudentsToModelAndShowViewWithAllStudents() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Student> expectedStudents = Arrays.asList(TestData.student1, TestData.student2);
        Page<Student> studentPage = new PageImpl<Student>(expectedStudents, pageable, expectedStudents.size());
        when(studentService.getAll(pageable)).thenReturn(studentPage);
        mockMvc.perform(get("/students")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/all"))
            .andExpect(model().attribute("studentPage", studentPage));
    }

    @Test
    public void whenGetById_thenAddStudentToModelAndShowViewWithStudent() throws Exception {
        when(studentService.getById(1)).thenReturn(TestData.student1);
        mockMvc.perform(get("/students/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("students/show"))
            .andExpect(model().attribute("student", TestData.student1));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        doNothing().when(studentService).create(TestData.student1);
        mockMvc.perform(get("/students/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(view().name("students/new"));
        mockMvc.perform(post("/students"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("student"))
            .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        when(studentService.getById(1)).thenReturn(TestData.student1);
        doNothing().when(studentService).update(TestData.student1);
        mockMvc.perform(get("/students/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/edit"))
            .andExpect(model().attribute("student", TestData.student1));
        mockMvc.perform(patch("/students/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        doNothing().when(studentService).delete(1);
        mockMvc.perform(delete("/students/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Student with id = 1 not found";
        when(studentService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/students/1"))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Student student1 = new Student.Builder()
            .setId(1)
            .build();
        Student student2 = new Student.Builder()
            .setId(2)
            .build();
    }
}
