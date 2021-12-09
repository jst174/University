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
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LessonControllerTest {

    private MockMvc mockMvc;
    @Mock
    private LessonService lessonService;
    @InjectMocks
    private LessonController lessonController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    public void whenGetAll_thenAddLessonsToModelAndShowViewWithAllLessons() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Lesson> expectedLessons = Arrays.asList(TestData.lesson1, TestData.lesson2);
        Page<Lesson> lessonPage = new PageImpl<Lesson>(expectedLessons, pageable, expectedLessons.size());
        when(lessonService.getAll(pageable)).thenReturn(lessonPage);
        mockMvc.perform(get("/lessons")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("lessons/all"))
            .andExpect(model().attribute("lessonPage", lessonPage));
    }

    @Test
    public void whenGetById_thenAddLessonToModelAndShowViewWithLesson() throws Exception {
        when(lessonService.getById(1)).thenReturn(TestData.lesson1);
        mockMvc.perform(get("/lessons/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("lessons/show"))
            .andExpect(model().attribute("lesson", TestData.lesson1));
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Lesson with id = 1 not found";
        when(lessonService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/lessons/1"))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Lesson lesson1 = new Lesson.Builder()
            .setId(1)
            .build();
        Lesson lesson2 = new Lesson.Builder()
            .setId(2)
            .build();
    }
}
