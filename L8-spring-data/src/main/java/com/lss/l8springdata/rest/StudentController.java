package com.lss.l8springdata.rest;

import com.lss.l8springdata.dto.StudentDTO;
import com.lss.l8springdata.entity.Student;
import com.lss.l8springdata.entity.StudentStatus;
import com.lss.l8springdata.service.EnrollmentService;
import com.lss.l8springdata.service.StudentService;
import com.lss.l8springdata.sql.QueryCounter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.createStudent(dto));
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        QueryCounter.increment();
        final ResponseEntity<List<StudentDTO>> response = ResponseEntity.ok(studentService.findAllDto());
        System.out.printf("💡 Total SQL queries: %s%n", QueryCounter.get());
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(@RequestParam String keyword) {
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Student> updateStudentStatus(@PathVariable Long id, @RequestParam StudentStatus status) {
        return ResponseEntity.ok(studentService.updateStatus(id, status));
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<String> enrollStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        enrollmentService.enrollStudent(studentId, courseId);
        return ResponseEntity.ok("Student enrolled successfully!");
    }

    @GetMapping("/with-enrollments")
    public ResponseEntity<List<Student>> getStudentsWithEnrollments() {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            System.out.println(student.getEnrollments().size());
        }
        return ResponseEntity.ok(students);
    }
}
