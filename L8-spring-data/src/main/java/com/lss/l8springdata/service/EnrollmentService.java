package com.lss.l8springdata.service;

import com.lss.l8springdata.entity.Enrollment;

public interface EnrollmentService {

    Enrollment enrollStudent(Long studentId, Long courseId);
}
