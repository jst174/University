package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.model.Group;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_groups_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcGroupDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcGroupDao groupDao;

    @Test
    public void givenNewGroup_whenCreate_thenCreated() {
        Group group = new Group("MJ-12");
        int expectedRows = countRowsInTable(jdbcTemplate, "groups") + 1;

        groupDao.create(group);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "groups"));

    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Group group = new Group("MJ-12");

        assertEquals(group, groupDao.getById(1));
    }

    @Test
    public void givenUpdatedCroupAndId_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM groups WHERE name = 'JD-32'";
        Group updatedGroup = new Group("JD-32");
        int beforeUpdate = countRowsInTableWhere(jdbcTemplate, "groups", SQL);
        updatedGroup.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "groups", SQL) + 1;

        groupDao.update(updatedGroup);

        int afterUpdate = countRowsInTableWhere(jdbcTemplate, "groups", SQL);
        assertEquals(expectedRows, afterUpdate);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "groups") - 1;

        groupDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "groups"));
    }

    @Test
    public void whenGetAll_thenReturnAllGroups(){
        Group group1 = new Group("MJ-12");
        Group group2 = new Group("FM-22");
        List<Group> expected = new ArrayList<>();
        expected.add(group1);
        expected.add(group2);

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }


}
