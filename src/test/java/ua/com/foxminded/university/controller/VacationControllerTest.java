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
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VacationControllerTest {

    private MockMvc mockMvc;
    @Mock
    private VacationService vacationService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private VacationController vacationController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void whenGetAll_thenAddVacationsToModelAndShowViewWithAllVacations() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Vacation> expectedVacations = Arrays.asList(TestData.vacation1, TestData.vacation2);
        Page<Vacation> vacationPage = new PageImpl<Vacation>(expectedVacations, pageable, expectedVacations.size());
        when(vacationService.getAll(pageable)).thenReturn(vacationPage);
        mockMvc.perform(get("/vacations")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("vacations/all"))
            .andExpect(model().attribute("vacationPage", vacationPage));
    }

    @Test
    public void whenGetById_thenAddVacationToModelAndShowViewWithVacation() throws Exception {
        when(vacationService.getById(1)).thenReturn(TestData.vacation1);
        mockMvc.perform(get("/vacations/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("vacations/show"))
            .andExpect(model().attribute("vacation", TestData.vacation1));
    }

    @Test
    public void whenShowCreatingForm_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/vacations/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vacation"))
            .andExpect(view().name("vacations/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        mockMvc.perform(post("/vacations")
                .param("start", "2021-12-12")
                .param("end", "2021-12-20"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("vacation"))
            .andExpect(view().name("redirect:/vacations"));
        verify(vacationService).create(TestData.vacation1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(vacationService.getById(1)).thenReturn(TestData.vacation1);
        mockMvc.perform(get("/vacations/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("vacations/edit"))
            .andExpect(model().attribute("vacation", TestData.vacation1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        mockMvc.perform(patch("/vacations/{id}", 1)
                .param("start", "2021-12-12")
                .param("end", "2021-12-20"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vacations"));
        verify(vacationService).update(TestData.vacation1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/vacations/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vacations"));
        verify(vacationService).delete(1);
    }

    @Test
    public void givenIncorrectGetRequest_whenGetById_thenShowExceptionView() throws Exception {
        String message = "Vacation with id = 1 not found";
        when(vacationService.getById(1)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(get("/vacations/1"))
            .andExpect(view().name("exception/error"))
            .andExpect(model().attribute("exception", "EntityNotFoundException"))
            .andExpect(model().attribute("message", message));
    }

    interface TestData {
        Vacation vacation1 = new Vacation.Builder()
            .setStart(LocalDate.of(2021, 12, 12))
            .setEnd(LocalDate.of(2021, 12, 20))
            .setId(1)
            .build();
        Vacation vacation2 = new Vacation.Builder()
            .setStart(LocalDate.of(2022, 1, 12))
            .setEnd(LocalDate.of(2022, 1, 20))
            .setId(2)
            .build();
    }
}
