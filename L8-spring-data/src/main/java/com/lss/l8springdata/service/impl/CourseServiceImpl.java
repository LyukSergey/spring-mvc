package com.lss.l8springdata.service.impl;

import com.lss.l8springdata.dto.CourseDTO;
import com.lss.l8springdata.entity.Course;
import com.lss.l8springdata.entity.Teacher;
import com.lss.l8springdata.exceptions.NotFoundException;
import com.lss.l8springdata.mappers.CourseMapper;
import com.lss.l8springdata.repository.CourseRepository;
import com.lss.l8springdata.repository.TeacherRepository;
import com.lss.l8springdata.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        final Long teacherId = dto.getTeacherId();
        final Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("teacher not found"));
        final Course course = Course.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .teacher(teacher)
                .build();
        return courseMapper.toDTO(courseRepository.save(course));
    }

    @Override
    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    public CourseDTO findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
