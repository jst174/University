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
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.exceptions.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public void givenNewGroup_whenCreate_thenCreated() throws NotUniqueNameException {
        Group group = new Group("GD-22");

        when(groupDao.getByName(group.getName())).thenReturn(Optional.empty());

        groupService.create(group);

        verify(groupDao).create(group);
    }

    @Test
    public void givenGroupWithExistentName_whenCreate_thenNotUniqueNameExceptionThrow() {
        Group group = new Group(groups.get(0).getName());

        when(groupDao.getByName(group.getName())).thenReturn(Optional.of(groups.get(0)));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.create(group));

        String expectedMessage = "Group with name = ND-12 already exist";

        verify(groupDao, never()).create(group);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentGroupId_whenGetById_thenReturn() throws EntityNotFoundException {
        Group group = groups.get(0);

        when(groupDao.getById(1)).thenReturn(Optional.of(group));

        assertEquals(group, groupService.getById(1));
    }

    @Test
    public void givenNotExistentGroupId_whenGetById_thenEntityNotFoundExceptionThrow() {
        when(groupDao.getById(20)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.getById(20));

        String expectedMessage = "Group with id = 20 not found";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentGroup_whenUpdate_thenUpdated() throws NotUniqueNameException {
        Group group = groups.get(0);

        when(groupDao.getByName(group.getName())).thenReturn(Optional.of(group));

        groupService.update(group);

        verify(groupDao).update(group);
    }

    @Test
    public void givenGroupWithOtherGroupName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        Group group1 = groups.get(0);
        Group group2 = groups.get(1);
        group1.setName(group2.getName());

        when(groupDao.getByName(group1.getName())).thenReturn(Optional.of(group2));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.update(group1));

        String expectedMessage = "Group with name = FR-32 already exist";

        verify(groupDao, never()).update(group1);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentGroupId_whenDelete_thenDeleted() {
        groupService.delete(1);

        verify(groupDao).delete(1);
    }

    @Test
    public void whenGetAll_thenReturn() {
        when(groupDao.getAll()).thenReturn(groups);

        assertEquals(groups, groupService.getAll());
    }

    @Test
    public void givenExistentLessonId_whenGetByLessonId_thenReturn() {
        when(groupDao.getByLessonId(1)).thenReturn(groups);

        assertEquals(groups, groupService.getByLessonId(1));
    }

}
