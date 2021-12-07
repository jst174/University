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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupControllerTest {

    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupController groupController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    public void whenGetAll_thenAddGroupToModelAndShowViewWithAllGroup() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        List<Group> expectedGroups = Arrays.asList(TestData.group1, TestData.group2);
        Page<Group> groupPage = new PageImpl<Group>(expectedGroups, pageable, expectedGroups.size());
        when(groupService.getAll(pageable)).thenReturn(groupPage);
        mockMvc.perform(get("/groups")
                .param("size", "10")
                .param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/all"))
            .andExpect(model().attribute("groupPage", groupPage));
    }

    @Test
    public void whenGetById_thenAddGroupToModelAndShowViewWithGroup() throws Exception {
        Group expectedGroups = TestData.group1;
        when(groupService.getById(1)).thenReturn(expectedGroups);
        mockMvc.perform(get("/groups/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/show"))
            .andExpect(model().attribute("group", expectedGroups));
    }


    interface TestData {
        Group group1 = new Group.Builder()
            .setName("GL-12")
            .setId(1)
            .build();
        Group group2 = new Group.Builder()
            .setName("FE-34")
            .setId(2)
            .build();
    }
}
