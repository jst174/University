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
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
            .setControllerAdvice(new ControllerExceptionHandler())
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
    public void whenShowCreatingForm_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/holidays/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("holiday"))
            .andExpect(view().name("holidays/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/holidays")
                .param("name", "New Year")
                .param("date", "2022-01-01"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("holiday"))
            .andExpect(view().name("redirect:/holidays"));
        verify(holidayService).create(TestData.holiday1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(holidayService.getById(1)).thenReturn(TestData.holiday1);
        mockMvc.perform(get("/holidays/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/edit"))
            .andExpect(model().attribute("holiday", TestData.holiday1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/holidays/{id}", 1)
                .param("name","New Year")
                .param("date","2022-01-01"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/holidays"));
        verify(holidayService).update(TestData.holiday1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/holidays/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/holidays"));
        verify(holidayService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Holiday with id = 1 not found";
        when(holidayService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/holidays/{id}", 1))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Holiday holiday1 = new Holiday.Builder()
            .setName("New Year")
            .setDate(LocalDate.of(2022, 1, 1))
            .setId(1)
            .build();
        Holiday holiday2 = new Holiday.Builder()
            .setName("Christmas")
            .setDate(LocalDate.of(2022, 1, 7))
            .setId(2)
            .build();
    }
}
