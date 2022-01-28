package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_lesson_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateGroupDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GroupDao groupDao;

    @Test
    public void givenNewGroup_whenCreate_thenCreated() {
        Group group = new Group("MJ-12");
        int expectedRows = countRowsInTable(jdbcTemplate, "groups") + 1;

        groupDao.create(group);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "groups"));

    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Group group = new Group("MH-12");

        assertEquals(group, groupDao.getById(1).get());
    }

    @Test
    public void givenUpdatedCroupAndId_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM groups WHERE name = 'JD-32'";
        Group updatedGroup = new Group("JD-32");
        int beforeUpdate = countRowsInTableWhere(jdbcTemplate, "groups", sql);
        updatedGroup.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "groups", sql) + 1;

        groupDao.update(updatedGroup);

        int afterUpdate = countRowsInTableWhere(jdbcTemplate, "groups", sql);
        assertEquals(expectedRows, afterUpdate);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "groups") - 1;

        groupDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "groups"));
    }

    @Test
    public void whenGetAll_thenReturnAllGroups() {
        Group group1 = new Group("MH-12");
        Group group2 = new Group("JW-23");
        Group group3 = new Group("MG-54");
        Group group4 = new Group("DF-23");
        Group group5 = new Group("GF-33");
        List<Group> expected = new ArrayList<>();
        expected.add(group1);
        expected.add(group2);
        expected.add(group3);
        expected.add(group4);
        expected.add(group5);

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturnAllGroups() {
        Group group1 = new Group("MH-12");
        Group group2 = new Group("JW-23");
        Group group3 = new Group("MG-54");
        Group group4 = new Group("DF-23");
        Group group5 = new Group("GF-33");
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);
        groups.add(group5);
        Pageable pageable = PageRequest.of(0, groups.size());
        Page<Group> groupPage = new PageImpl<Group>(groups, pageable, groups.size());

        assertEquals(groupPage, groupDao.getAll(pageable));
    }

    @Test
    public void givenGroupName_whenGetByName_thenReturn() {
        Group group = new Group("MH-12");

        assertEquals(group, groupDao.getByName("MH-12").get());
    }


}
