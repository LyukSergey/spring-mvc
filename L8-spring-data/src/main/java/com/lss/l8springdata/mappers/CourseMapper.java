package com.lss.l8springdata.mappers;

import com.lss.l8springdata.dto.CourseDTO;
import com.lss.l8springdata.entity.Course;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "enrolledStudentIds", expression = "java(mapStudentIds(course))")
    CourseDTO toDTO(Course course);

    default List<Long> mapStudentIds(Course course) {
        if (course.getEnrollments() == null) return List.of();
        return course.getEnrollments().stream()
                .map(e -> e.getStudent().getId())
                .collect(Collectors.toList());
    }
}
