package ua.com.foxminded.university.dto;

import org.mapstruct.*;
import ua.com.foxminded.university.model.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "title", source = "lesson.course.name")
    @Mapping(target = "start", expression = "java(lesson.getDate().atTime(lesson.getTime().getStartTime()))")
    @Mapping(target = "end", expression = "java(lesson.getDate().atTime(lesson.getTime().getEndTime()))")
    LessonDto convertLessonToLessonDto(Lesson lesson);

}
