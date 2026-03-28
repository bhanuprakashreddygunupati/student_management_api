package com.studentapi.service;

import com.studentapi.dto.PagedResponse;
import com.studentapi.dto.StudentRequestDTO;
import com.studentapi.dto.StudentResponseDTO;
import java.util.List;

public interface StudentService {

    // Create student
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    // Get all students
    List<StudentResponseDTO> getAllStudents();

    // Get all students with pagination
    PagedResponse<StudentResponseDTO> getAllStudentsPaginated(
            int pageNumber, int pageSize, String sortBy, String sortDirection);

    // Get student by ID
    StudentResponseDTO getStudentById(Long id);

    // Get student by email
    StudentResponseDTO getStudentByEmail(String email);

    // Update student
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO);

    // Partial update student
    StudentResponseDTO partialUpdateStudent(Long id, StudentRequestDTO requestDTO);

    // Delete student
    String deleteStudent(Long id);

    // Search students by name
    List<StudentResponseDTO> searchStudentsByName(String name);

    // Get students by course
    List<StudentResponseDTO> getStudentsByCourse(String course);

    // Get total count
    long getTotalStudentCount();

    // Check if email exists
    boolean isEmailExists(String email);
}