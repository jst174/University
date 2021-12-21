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
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CourseService courseService;
    @InjectMocks
    private CourseController courseController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddCoursesToModelAndShowViewWithAllCourses() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Course> expectedCourse = Arrays.asList(TestData.course1, TestData.course2);
        Page<Course> coursePage = new PageImpl<Course>(expectedCourse, pageable, expectedCourse.size());
        when(courseService.getAll(pageable)).thenReturn(coursePage);
        mockMvc.perform(get("/courses")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/all"))
            .andExpect(model().attribute("coursePage", coursePage));
    }

    @Test
    public void whenGetById_thenAddCourseToModelAndShowViewWithCourse() throws Exception {
        when(courseService.getById(1)).thenReturn(TestData.course1);
        mockMvc.perform(get("/courses/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/show"))
            .andExpect(model().attribute("course", TestData.course1));
    }

    @Test
    public void whenShowCreatingForm_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/courses/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("course"))
            .andExpect(view().name("courses/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        courseService.create(TestData.course1);
        mockMvc.perform(post("/courses"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("course"))
            .andExpect(view().name("redirect:/courses"));
        verify(courseService).create(TestData.course1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(courseService.getById(1)).thenReturn(TestData.course1);
        mockMvc.perform(get("/courses/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/edit"))
            .andExpect(model().attribute("course", TestData.course1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        courseService.update(TestData.course1);
        mockMvc.perform(patch("/courses/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/courses"));
        verify(courseService).update(TestData.course1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/courses"));
        verify(courseService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Course with id = 1 not found";
        when(courseService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/courses/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Course course1 = new Course.Builder()
            .setName("History")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Math")
            .setId(2)
            .build();
    }
}
