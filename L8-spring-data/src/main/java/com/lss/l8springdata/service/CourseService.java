package com.lss.l8springdata.service;

import com.lss.l8springdata.dto.CourseDTO;
import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO dto);

    List<CourseDTO> findAll();

    CourseDTO findById(Long id);
}
