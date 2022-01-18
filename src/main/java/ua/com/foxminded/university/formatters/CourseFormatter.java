package ua.com.foxminded.university.formatters;

import org.springframework.format.Formatter;
import ua.com.foxminded.university.model.Course;

import java.util.Locale;

public class CourseFormatter implements Formatter<Course> {
    
    @Override
    public Course parse(String text, Locale locale){
        Course course = new Course();
        if (text != null) {
            String[] parts = text.split(",");
            course.setId(Integer.parseInt(parts[0]));
            if (parts.length > 1) {
                course.setName(parts[1]);
            }
        }
        return course;
    }

    @Override
    public String print(Course course, Locale locale) {
        return course.toString();
    }
}
