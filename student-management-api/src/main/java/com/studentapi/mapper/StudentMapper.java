package com.studentapi.mapper;

import com.studentapi.dto.StudentRequestDTO;
import com.studentapi.dto.StudentResponseDTO;
import com.studentapi.entity.Student;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    // Convert Entity to Response DTO
    public StudentResponseDTO toResponseDTO(Student student) {
        if (student == null) {
            return null;
        }

        return StudentResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .course(student.getCourse())
                .phone(student.getPhone())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    // Convert Request DTO to Entity
    public Student toEntity(StudentRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Student student = new Student();
        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setCourse(requestDTO.getCourse());
        student.setPhone(requestDTO.getPhone());
        return student;
    }

    // Convert List of Entities to List of Response DTOs
    public List<StudentResponseDTO> toResponseDTOList(List<Student> students) {
        return students.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update Entity from Request DTO
    public void updateEntityFromDTO(StudentRequestDTO requestDTO, Student student) {
        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setCourse(requestDTO.getCourse());
        student.setPhone(requestDTO.getPhone());
    }
}