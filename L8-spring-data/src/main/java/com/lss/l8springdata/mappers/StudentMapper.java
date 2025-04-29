package com.lss.l8springdata.mappers;

import com.lss.l8springdata.dto.StudentDTO;
import com.lss.l8springdata.entity.Student;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "enrolledCourseIds", expression = "java(mapCourseIds(student))")
    StudentDTO toDTO(Student student);

    default List<Long> mapCourseIds(Student student) {
        if (student.getEnrollments() == null) return List.of();
        return student.getEnrollments().stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toList());
    }
}
