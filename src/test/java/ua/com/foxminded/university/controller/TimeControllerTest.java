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
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Time;
import ua.com.foxminded.university.service.TimeService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimeControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TimeService timeService;
    @InjectMocks
    private TimeController timeController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(timeController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    public void whenGetAll_thenAddTimesToModelAndShowViewWithAllTimes() throws Exception {
        List<Time> expectedTimes = Arrays.asList(TestData.time1, TestData.time2);
        when(timeService.getAll()).thenReturn(expectedTimes);
        mockMvc.perform(get("/times"))
            .andExpect(status().isOk())
            .andExpect(view().name("times/all"))
            .andExpect(model().attribute("times", expectedTimes));
    }

    @Test
    public void whenGetById_thenAddTimeToModelAndShowViewWithTime() throws Exception {
        Time expectedTime = TestData.time1;
        when(timeService.getById(1)).thenReturn(expectedTime);
        mockMvc.perform(get("/times/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("times/show"))
            .andExpect(model().attribute("time", expectedTime));
    }

    interface TestData {
        Time time1 = new Time.Builder()
            .setId(1)
            .build();
        Time time2 = new Time.Builder()
            .setId(2)
            .build();
    }
}
