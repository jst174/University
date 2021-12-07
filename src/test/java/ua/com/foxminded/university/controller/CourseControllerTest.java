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
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Course expectedCourse = TestData.course1;
        when(courseService.getById(1)).thenReturn(expectedCourse);
        mockMvc.perform(get("/courses/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/show"))
            .andExpect(model().attribute("course", expectedCourse));
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
