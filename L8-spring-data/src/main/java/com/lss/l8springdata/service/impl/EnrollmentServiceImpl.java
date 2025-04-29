package com.lss.l8springdata.service.impl;

import com.lss.l8springdata.entity.Course;
import com.lss.l8springdata.entity.Enrollment;
import com.lss.l8springdata.entity.Student;
import com.lss.l8springdata.exceptions.NotFoundException;
import com.lss.l8springdata.exceptions.ValidationException;
import com.lss.l8springdata.repository.CourseRepository;
import com.lss.l8springdata.repository.EnrollmentRepository;
import com.lss.l8springdata.repository.StudentRepository;
import com.lss.l8springdata.service.EnrollmentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        final Optional<Enrollment> existingEnrolment = student.getEnrollments().stream()
                .filter(enrollment -> enrollment.getId().equals(courseId))
                .findFirst();

        if (existingEnrolment.isPresent()) {
            throw new ValidationException("Student already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

}
