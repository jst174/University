package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private LessonDao lessonDao;
    @InjectMocks
    private GroupService groupService;
    private List<Group> groups;
    private List<Lesson> lessons;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        groups = new ArrayList<>();
        Group group1 = new Group("ND-12");
        group1.setId(1);
        Group group2 = new Group("FR-32");
        group2.setId(2);
        groups.add(group1);
        groups.add(group2);
        lessons = new ArrayList<>();
        DataSource dataSource = new DataSource();
        Lesson lesson1 = dataSource.generateLesson(LocalDate.of(2021, 12, 01), groups);
        lesson1.setId(1);
        Lesson lesson2 = dataSource.generateLesson(LocalDate.of(2021, 12, 04), dataSource.getGroups());
        lesson2.setId(2);
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    public void givenNewGroup_whenCreate_thenCreated() {
        Group group = new Group("GD-22");

        when(groupDao.getByName("GD-22")).thenReturn(group);

        groupService.create(group);

        verify(groupDao).create(group);
    }

    @Test
    public void givenExistentGroupId_whenGetById_thenReturn() {
        Group group = groups.get(0);

        when(groupDao.getById(1)).thenReturn(group);

        assertEquals(group, groupService.getById(1));
    }

    @Test
    public void givenExistentGroup_whenUpdate_thenUpdated() {
        Group group = groups.get(0);

        when(groupDao.getById(1)).thenReturn(group);

        groupService.update(group);

        verify(groupDao).update(group);
    }

    @Test
    public void givenExistentGroupId_whenDelete_thenDeleted() {
        groupService.delete(1);

        verify(groupDao).delete(1);
    }

    @Test
    public void givenExistentLessonId_whenGetByLessonId_thenReturn() {
        when(groupDao.getByLessonId(1)).thenReturn(groups);

        assertEquals(groups, groupService.getByLessonId(1));
    }

}
