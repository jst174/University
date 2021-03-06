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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
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
    public void whenShowCreatingFrom_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/students/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(view().name("students/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/students")
                .param("firstName", "Mike")
                .param("lastName", "Miller")
                .param("gender", "MALE")
                .param("birthDate", "1994-11-12")
            )
            .andExpect(status().is3xxRedirection())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(model().attributeExists("student"))
            .andExpect(view().name("redirect:/students"));
        verify(studentService).create(TestData.student1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(studentService.getById(1)).thenReturn(TestData.student1);
        mockMvc.perform(get("/students/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("students/edit"))
            .andExpect(model().attribute("student", TestData.student1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/students/{id}", 1)
                .param("firstName", "Mike")
                .param("lastName", "Miller")
                .param("gender", "MALE")
                .param("birthDate", "1994-11-12"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/students"));
        verify(studentService).update(TestData.student1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/students/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/students"));
        verify(studentService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Student with id = 1 not found";
        when(studentService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/students/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Student student1 = new Student.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setId(1)
            .build();
        Student student2 = new Student.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
            .build();
    }
}
