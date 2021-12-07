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
import ua.com.foxminded.university.service.ClassroomService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClassroomControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ClassroomService classroomService;
    @InjectMocks
    private ClassroomController classroomController;

    @BeforeAll
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
        mockMvc.perform(get("/classrooms")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/all"))
            .andExpect(model().attribute("classroomPage", classroomPage));
    }

    @Test
    public void whenGetById_thenAddClassroomToModelAndShowViewWithClassroom() throws Exception {
        Classroom expectedClassroom = TestData.classroom1;
        when(classroomService.getById(1)).thenReturn(expectedClassroom);
        mockMvc.perform(get("/classrooms/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/show"))
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
