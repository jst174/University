package ua.com.foxminded.university.dto;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ua.com.foxminded.university.model.Lesson;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonMapper mapper = Mappers.getMapper(LessonMapper.class);

    @Mappings({
        @Mapping(target = "title", source = "lesson.course.name"),
        @Mapping(target = "start", expression = "java(lesson.getDate().atTime(lesson.getTime().getStartTime()).toString())"),
        @Mapping(target = "end", expression = "java(lesson.getDate().atTime(lesson.getTime().getEndTime()).toString())")
    })
    LessonDto convertLessonToLessonDto(Lesson lesson);

        default List<LessonDto> convertToDtoList(List<Lesson> lessons) {
        List<LessonDto> lessonsDto = new ArrayList<>();
        LessonMapper mapper = Mappers.getMapper(LessonMapper.class);
        for (Lesson lesson : lessons) {
            LessonDto lessonDto = mapper.convertLessonToLessonDto(lesson);
            lessonsDto.add(lessonDto);
        }
        return lessonsDto;
    }

}
