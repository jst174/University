package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.NotUniqueNameException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @InjectMocks
    private GroupService groupService;

    @Test
    public void givenNewGroup_whenCreate_thenCreated() throws NotUniqueNameException {
        Group group = new Group("GD-22");
        when(groupDao.getByName(group.getName())).thenReturn(Optional.empty());

        groupService.create(group);

        verify(groupDao).create(group);
    }

    @Test
    public void givenGroupWithExistentName_whenCreate_thenNotUniqueNameExceptionThrow() {
        Group group = new Group(TestData.group1.getName());
        when(groupDao.getByName(group.getName())).thenReturn(Optional.of(TestData.group1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.create(group));

        String expectedMessage = "Group with name = ND-12 already exist";
        verify(groupDao, never()).create(group);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentGroupId_whenGetById_thenReturn() throws EntityNotFoundException {
        when(groupDao.getById(1)).thenReturn(Optional.of(TestData.group1));

        assertEquals(TestData.group1, groupService.getById(1));
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
        when(groupDao.getByName(TestData.group1.getName())).thenReturn(Optional.of(TestData.group1));

        groupService.update(TestData.group1);

        verify(groupDao).update(TestData.group1);
    }

    @Test
    public void givenGroupWithOtherGroupName_whenUpdate_thenNotUniqueNameExceptionThrow() {
        when(groupDao.getByName(TestData.group2.getName())).thenReturn(Optional.of(TestData.group1));

        Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.update(TestData.group2));

        String expectedMessage = "Group with name = FR-32 already exist";
        verify(groupDao, never()).update(TestData.group2);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void givenExistentGroupId_whenDelete_thenDeleted() {
        groupService.delete(1);

        verify(groupDao).delete(1);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        List<Group> groups = Arrays.asList(TestData.group1, TestData.group2);
        Pageable pageable = PageRequest.of(1, 10);
        Page<Group> groupPage = new PageImpl<Group>(groups, pageable, groups.size());
        when(groupDao.getAll(pageable)).thenReturn(groupPage);

        assertEquals(groupPage, groupService.getAll(pageable));
    }

    @Test
    public void whenGetAll_thenReturn() {
        List<Group> groups = Arrays.asList(TestData.group1, TestData.group2);
        when(groupDao.getAll()).thenReturn(groups);

        assertEquals(groups, groupService.getAll());
    }

    @Test
    public void givenExistentLessonId_whenGetByLessonId_thenReturn() {
        List<Group> groups = Arrays.asList(TestData.group1, TestData.group2);
        when(groupDao.getByLessonId(1)).thenReturn(groups);

        assertEquals(groups, groupService.getByLessonId(1));
    }

    interface TestData {
        Group group1 = new Group.Builder()
            .setName("ND-12")
            .setId(1)
            .build();
        Group group2 = new Group.Builder()
            .setName("FR-32")
            .setId(2)
            .build();
    }

}
