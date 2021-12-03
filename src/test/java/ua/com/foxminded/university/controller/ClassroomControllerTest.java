package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import ua.com.foxminded.university.config.SpringMvcConfig;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringMvcConfig.class})
@WebAppConfiguration
public class ClassroomControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ClassroomService classroomService;
    @InjectMocks
    private ClassroomController classroomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(classroomController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    public void whenGetAll_thenAddClassroomsToModelAndShowViewWithAllClassrooms() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Classroom> expectedClassrooms = Arrays.asList(TestData.classroom1, TestData.classroom2);
        Page<Classroom> classroomPage = new PageImpl<Classroom>(expectedClassrooms, pageable, expectedClassrooms.size());
        when(classroomService.getAll(pageable)).thenReturn(classroomPage);
        mockMvc.perform(get("/classrooms?size=10&page=1"))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/all"))
            .andExpect(model().attributeExists("classroomPage"))
            .andExpect(model().attribute("classroomPage", classroomPage));
    }

    @Test
    public void whenGetById_thenAddClassroomToModelAndShowViewWithClassroom() throws Exception {
        Classroom expectedClassroom = TestData.classroom1;
        when(classroomService.getById(1)).thenReturn(expectedClassroom);
        mockMvc.perform(get("/classrooms/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/show"))
            .andExpect(model().attributeExists("classroom"))
            .andExpect(model().attribute("classroom", expectedClassroom));
    }

    interface TestData {
        Classroom classroom1 = new Classroom.Builder()
            .setNumber(102)
            .setCapacity(30)
            .setId(1)
            .build();
        Classroom classroom2 = new Classroom.Builder()
            .setNumber(202)
            .setCapacity(50)
            .setId(2)
            .build();
    }
}
