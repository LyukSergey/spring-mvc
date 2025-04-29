package com.lss.l8springdata.service.impl;

import com.lss.l8springdata.dto.StudentDTO;
import com.lss.l8springdata.entity.Student;
import com.lss.l8springdata.entity.StudentStatus;
import com.lss.l8springdata.exceptions.NotFoundException;
import com.lss.l8springdata.mappers.StudentMapper;
import com.lss.l8springdata.repository.StudentRepository;
import com.lss.l8springdata.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO dto) {
        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        return studentMapper.toDTO(studentRepository.save(student));
    }

    @Override
    @Transactional
    public Student updateStatus(Long studentId, StudentStatus newStatus) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        student.setStatus(newStatus);
        return studentRepository.save(student);
    }

    @Override
    public List<StudentDTO> searchStudents(String keyword) {
        return studentRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    public List<StudentDTO> findAllDto() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

}
