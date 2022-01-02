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
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LessonControllerTest {

    private MockMvc mockMvc;
    @Mock
    private LessonService lessonService;
    @Mock
    private CourseService courseService;
    @Mock
    private GroupService groupService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TimeService timeService;
    @Mock
    private ClassroomService classroomService;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private LessonController lessonController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ControllerExceptionHandler())
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

    @Test
    public void whenShowCreatingFrom_thenShowCreatingForm() throws Exception {
        mockMvc.perform(get("/lessons/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(view().name("lessons/new"));
    }

    @Test
    public void whenCreate_thenCreatedAndRedirectView() throws Exception {
        when(groupService.getById(1)).thenReturn(TestData.group1);
        when(studentService.getByGroupId(1)).thenReturn(new ArrayList<>(Arrays.asList(TestData.student1, TestData.student2)));
        when(classroomService.getById(1)).thenReturn(TestData.classroom1);
        when(timeService.getById(1)).thenReturn(TestData.time1);
        when(teacherService.getById(1)).thenReturn(TestData.teacher1);
        when(courseService.getById(1)).thenReturn(TestData.course1);
        mockMvc.perform(post("/lessons"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(view().name("redirect:/lessons"));
        verify(lessonService).create(TestData.lesson1);
    }

    @Test
    public void whenEdit_thenShowEditingForm() throws Exception {
        when(lessonService.getById(1)).thenReturn(TestData.lesson1);
        when(courseService.getAll()).thenReturn(Arrays.asList(TestData.course1, TestData.course2));
        when(teacherService.getAll()).thenReturn(Arrays.asList(TestData.teacher1, TestData.teacher2));
        when(groupService.getAll()).thenReturn(Arrays.asList(TestData.group1, TestData.group2));
        when(classroomService.getAll()).thenReturn(Arrays.asList(TestData.classroom1, TestData.classroom2));
        when(timeService.getAll()).thenReturn(Arrays.asList(TestData.time1, TestData.time2));
        mockMvc.perform(get("/lessons/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("lessons/edit"))
            .andExpect(model().attribute("lesson", TestData.lesson1));
    }

    @Test
    public void whenUpdate_thenUpdateAndRedirectView() throws Exception {
        when(lessonService.getById(1)).thenReturn(TestData.lesson1);
        when(groupService.getById(1)).thenReturn(TestData.group1);
        when(studentService.getByGroupId(1)).thenReturn(Arrays.asList(TestData.student1, TestData.student2));
        when(classroomService.getById(1)).thenReturn(TestData.classroom1);
        when(timeService.getById(1)).thenReturn(TestData.time1);
        when(teacherService.getById(1)).thenReturn(TestData.teacher1);
        when(courseService.getById(1)).thenReturn(TestData.course1);
        mockMvc.perform(post("/lessons"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(view().name("redirect:/lessons"));
        verify(lessonService).update(TestData.lesson1);
    }

    @Test
    public void whenDelete_thenDeleteClassroomAndRedirectView() throws Exception {
        mockMvc.perform(delete("/lessons/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/lessons"));
        verify(lessonService).delete(1);
    }

    interface TestData {
        Student student1 = new Student.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setId(1)
            .build();
        Student student2 = new Student.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
            .build();
        Group group1 = new Group.Builder()
            .setName("GD-12")
            .setId(1)
            .build();
        Group group2 = new Group.Builder()
            .setName("GS-14")
            .setId(2)
            .build();
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1994, 11, 12))
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Tom")
            .setLastName("Price")
            .setGender(Gender.MALE)
            .setBirtDate(LocalDate.of(1995, 10, 11))
            .setId(2)
            .build();
        Course course1 = new Course.Builder()
            .setName("History")
            .setId(1)
            .build();
        Course course2 = new Course.Builder()
            .setName("Math")
            .setId(2)
            .build();
        Time time1 = new Time.Builder()
            .setStartTime(LocalTime.of(8, 0))
            .setEndTime(LocalTime.of(9, 30))
            .setId(1)
            .build();
        Time time2 = new Time.Builder()
            .setStartTime(LocalTime.of(12, 0))
            .setEndTime(LocalTime.of(13, 30))
            .setId(2)
            .build();
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
        Lesson lesson1 = new Lesson.Builder()
            .setDate(LocalDate.of(2021, 12, 12))
            .setTime(time1)
            .setClassroom(classroom1)
            .setCourse(course1)
            .setTeacher(teacher1)
            .setGroups(new ArrayList<>(Arrays.asList(TestData.group1, TestData.group2)))
            .setId(1)
            .build();
        Lesson lesson2 = new Lesson.Builder()
            .setDate(LocalDate.of(2021, 12, 13))
            .setTime(time2)
            .setClassroom(classroom2)
            .setTeacher(teacher2)
            .setCourse(course2)
            .setId(2)
            .build();
    }
}
