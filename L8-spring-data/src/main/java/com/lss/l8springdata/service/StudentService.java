package com.lss.l8springdata.service;

import com.lss.l8springdata.dto.StudentDTO;
import com.lss.l8springdata.entity.Student;
import com.lss.l8springdata.entity.StudentStatus;
import java.util.List;

public interface StudentService {

    StudentDTO createStudent(StudentDTO dto);

    Student updateStatus(Long studentId, StudentStatus newStatus);

    List<StudentDTO> searchStudents(String keyword);

    List<StudentDTO> findAllDto();

    List<Student> findAll();
}
