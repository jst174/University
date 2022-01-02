package ua.com.foxminded.university.formatters;

import org.springframework.format.Formatter;
import ua.com.foxminded.university.model.Group;

import java.text.ParseException;
import java.util.Locale;

public class GroupFormatter implements Formatter<Group> {
    @Override
    public Group parse(String id, Locale locale) throws ParseException {
        Group group = new Group();
        group.setId(Integer.parseInt(id));
        return group;
    }

    @Override
    public String print(Group group, Locale locale) {
        return Integer.toString(group.getId());
    }
}
