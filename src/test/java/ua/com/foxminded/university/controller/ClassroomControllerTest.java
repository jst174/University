package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.config.SpringMvcConfig;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();
    }

    @Test
    public void whenGetAll_thenAddClassroomsToModelAndShowViewWithAllClassrooms() throws Exception {
        when(classroomService.getAll()).thenReturn(Arrays.asList(TestData.classroom1, TestData.classroom2));

        mockMvc.perform(get("/classrooms"))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/all"))
            .andExpect(model().attribute("classrooms", hasSize(2)))
            .andExpect(model().attribute("classrooms", hasItem(
                allOf(
                    hasProperty("id", is(1)),
                    hasProperty("number", is(102)),
                    hasProperty("capacity", is(30))
                )
            )))
            .andExpect(model().attribute("classrooms", hasItem(
                allOf(
                    hasProperty("id", is(2)),
                    hasProperty("number", is(202)),
                    hasProperty("capacity", is(50))
                )
            )));

        verify(classroomService).getAll();
        verifyNoMoreInteractions(classroomService);
    }

    @Test
    public void whenGetById_thenAddClassroomToModelAndShowViewWithClassroom() throws Exception {
        when(classroomService.getById(1)).thenReturn(TestData.classroom1);

        mockMvc.perform(get("/classrooms/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("classrooms/show"))
            .andExpect(model().attribute("classroom", hasProperty("id", is(1))))
            .andExpect(model().attribute("classroom", hasProperty("number", is(102))))
            .andExpect(model().attribute("classroom", hasProperty("capacity", is(30))));

        verify(classroomService).getById(1);
        verifyNoMoreInteractions(classroomService);
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
