package com.studentapi.controller;

import com.studentapi.constants.AppConstants;
import com.studentapi.dto.ApiResponse;
import com.studentapi.dto.PagedResponse;
import com.studentapi.dto.StudentRequestDTO;
import com.studentapi.dto.StudentResponseDTO;
import com.studentapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ==================== CREATE STUDENT ====================
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO response = studentService.createStudent(requestDTO);
        
        return new ResponseEntity<>(
                ApiResponse.created(response, AppConstants.STUDENT_CREATED),
                HttpStatus.CREATED
        );
    }

    // ==================== GET ALL STUDENTS ====================
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {

        List<StudentResponseDTO> students = studentService.getAllStudents();
        
        return ResponseEntity.ok(
                ApiResponse.success(students, AppConstants.STUDENTS_FOUND)
        );
    }

    // ==================== GET ALL STUDENTS WITH PAGINATION ====================
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PagedResponse<StudentResponseDTO>>> getAllStudentsPaginated(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDirection) {

        PagedResponse<StudentResponseDTO> pagedResponse = 
                studentService.getAllStudentsPaginated(pageNumber, pageSize, sortBy, sortDirection);
        
        return ResponseEntity.ok(
                ApiResponse.success(pagedResponse, AppConstants.STUDENTS_FOUND)
        );
    }

    // ==================== GET STUDENT BY ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(
            @PathVariable Long id) {

        StudentResponseDTO student = studentService.getStudentById(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(student, AppConstants.STUDENT_FOUND)
        );
    }

    // ==================== GET STUDENT BY EMAIL ====================
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByEmail(
            @PathVariable String email) {

        StudentResponseDTO student = studentService.getStudentByEmail(email);
        
        return ResponseEntity.ok(
                ApiResponse.success(student, AppConstants.STUDENT_FOUND)
        );
    }

    // ==================== UPDATE STUDENT (FULL UPDATE) ====================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO response = studentService.updateStudent(id, requestDTO);
        
        return ResponseEntity.ok(
                ApiResponse.success(response, AppConstants.STUDENT_UPDATED)
        );
    }

    // ==================== PARTIAL UPDATE STUDENT (PATCH) ====================
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> partialUpdateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO response = studentService.partialUpdateStudent(id, requestDTO);
        
        return ResponseEntity.ok(
                ApiResponse.success(response, AppConstants.STUDENT_UPDATED)
        );
    }

    // ==================== DELETE STUDENT ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {

        String message = studentService.deleteStudent(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(message, AppConstants.STUDENT_DELETED)
        );
    }

    // ==================== SEARCH STUDENTS BY NAME ====================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudents(
            @RequestParam String name) {

        List<StudentResponseDTO> students = studentService.searchStudentsByName(name);
        
        return ResponseEntity.ok(
                ApiResponse.success(students, AppConstants.STUDENTS_FOUND)
        );
    }

    // ==================== GET STUDENTS BY COURSE ====================
    @GetMapping("/course/{course}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByCourse(
            @PathVariable String course) {

        List<StudentResponseDTO> students = studentService.getStudentsByCourse(course);
        
        return ResponseEntity.ok(
                ApiResponse.success(students, AppConstants.STUDENTS_FOUND)
        );
    }

    // ==================== GET TOTAL COUNT ====================
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalStudentCount() {

        long count = studentService.getTotalStudentCount();
        
        return ResponseEntity.ok(
                ApiResponse.success(count, "Total student count retrieved")
        );
    }

    // ==================== CHECK EMAIL EXISTS ====================
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(
            @RequestParam String email) {

        boolean exists = studentService.isEmailExists(email);
        
        return ResponseEntity.ok(
                ApiResponse.success(exists, "Email check completed")
        );
    }
}