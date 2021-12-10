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
import ua.com.foxminded.university.errors.ResponseExceptionHandler;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HolidayControllerTest {

    private MockMvc mockMvc;
    @Mock
    private HolidayService holidayService;
    @InjectMocks
    private HolidayController holidayController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ResponseExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddHolidaysToModelAndShowViewWithAllHolidays() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Holiday> expectedHolidays = Arrays.asList(TestData.holiday1, TestData.holiday2);
        Page<Holiday> holidayPage = new PageImpl<Holiday>(expectedHolidays, pageable, expectedHolidays.size());
        when(holidayService.getAll(pageable)).thenReturn(holidayPage);
        mockMvc.perform(get("/holidays")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/all"))
            .andExpect(model().attribute("holidayPage", holidayPage));
    }

    @Test
    public void whenGetById_thenAddHolidayToModelAndShowViewWithHoliday() throws Exception {
        when(holidayService.getById(1)).thenReturn(TestData.holiday1);
        mockMvc.perform(get("/holidays/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/show"))
            .andExpect(model().attribute("holiday", TestData.holiday1));
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Holiday with id = 1 not found";
        when(holidayService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/holidays/1"))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Holiday holiday1 = new Holiday.Builder()
            .setId(1)
            .build();
        Holiday holiday2 = new Holiday.Builder()
            .setId(2)
            .build();
    }
}
