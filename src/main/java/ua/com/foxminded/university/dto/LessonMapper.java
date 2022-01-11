package ua.com.foxminded.university.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ua.com.foxminded.university.model.Lesson;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface LessonMapper {

    LessonMapper mapper = Mappers.getMapper(LessonMapper.class);

    @Mappings({
        @Mapping(target = "title", expression = "java(entity.getCourse().getName() + \" \" " +
            "+ entity.getTime().getStartTime().toString() + \"-\"" +
            "+ entity.getTime().getEndTime().toString())"),
        @Mapping(target = "start", source = "entity.date", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "end", source = "entity.date", dateFormat = "yyyy-MM-dd")
    })
    LessonDto convertLessonToLessonDto(Lesson entity);

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
