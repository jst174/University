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
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
            .setControllerAdvice(new ControllerExceptionHandler())
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
        when(classroomService.getById(1)).thenReturn(TestData.classroom1);
        mockMvc.perform(get("/classrooms/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/show"))
            .andExpect(model().attribute("classroom", TestData.classroom1));
    }

    @Test
    public void whenShowCreationForm_thenShowCreationForm() throws Exception {
        mockMvc.perform(get("/classrooms/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("classroom"))
            .andExpect(view().name("classrooms/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/classrooms")
                .param("number", "101")
                .param("capacity", "30"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("classroom"))
            .andExpect(view().name("redirect:/classrooms"));
        verify(classroomService).createClassroom(TestData.classroom1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(classroomService.getById(1)).thenReturn(TestData.classroom1);
        mockMvc.perform(get("/classrooms/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/edit"))
            .andExpect(model().attribute("classroom", TestData.classroom1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/classrooms/{id}", 1)
                .param("number", "101")
                .param("capacity", "30"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/classrooms"));
        verify(classroomService).update(TestData.classroom1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/classrooms/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/classrooms"));
        verify(classroomService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Classroom with id = 1 not found";
        when(classroomService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/classrooms/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attributeExists("exception"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
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
    }
}
