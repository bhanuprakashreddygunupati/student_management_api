package com.studentapi.service;

import com.studentapi.constants.AppConstants;
import com.studentapi.dto.PagedResponse;
import com.studentapi.dto.StudentRequestDTO;
import com.studentapi.dto.StudentResponseDTO;
import com.studentapi.entity.Student;
import com.studentapi.exception.DuplicateEmailException;
import com.studentapi.exception.StudentNotFoundException;
import com.studentapi.mapper.StudentMapper;
import com.studentapi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    // ==================== CREATE ====================
    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        logger.info("Creating new student with email: {}", requestDTO.getEmail());

        // Check if email already exists
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            logger.error("Email already exists: {}", requestDTO.getEmail());
            throw new DuplicateEmailException(AppConstants.EMAIL_EXISTS + requestDTO.getEmail());
        }

        // Convert DTO to Entity
        Student student = studentMapper.toEntity(requestDTO);

        // Save to database
        Student savedStudent = studentRepository.save(student);
        logger.info("Student created successfully with ID: {}", savedStudent.getId());

        return studentMapper.toResponseDTO(savedStudent);
    }

    // ==================== GET ALL ====================
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        logger.info("Fetching all students");

        List<Student> students = studentRepository.findAll();
        logger.info("Found {} students", students.size());

        return studentMapper.toResponseDTOList(students);
    }

    // ==================== GET ALL WITH PAGINATION ====================
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentResponseDTO> getAllStudentsPaginated(
            int pageNumber, int pageSize, String sortBy, String sortDirection) {

        logger.info("Fetching students - Page: {}, Size: {}, Sort: {} {}", 
                pageNumber, pageSize, sortBy, sortDirection);

        // Create Sort object
        Sort sort = sortDirection.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();

        // Create Pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Fetch page from repository
        Page<Student> studentPage = studentRepository.findAll(pageable);

        // Convert to DTOs
        List<StudentResponseDTO> content = studentMapper.toResponseDTOList(studentPage.getContent());

        // Build paged response
        return PagedResponse.<StudentResponseDTO>builder()
                .content(content)
                .pageNumber(studentPage.getNumber())
                .pageSize(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .first(studentPage.isFirst())
                .last(studentPage.isLast())
                .hasNext(studentPage.hasNext())
                .hasPrevious(studentPage.hasPrevious())
                .build();
    }

    // ==================== GET BY ID ====================
    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        logger.info("Fetching student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new StudentNotFoundException(AppConstants.STUDENT_NOT_FOUND + id);
                });

        return studentMapper.toResponseDTO(student);
    }

    // ==================== GET BY EMAIL ====================
    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByEmail(String email) {
        logger.info("Fetching student with email: {}", email);

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Student not found with email: {}", email);
                    return new StudentNotFoundException("Student not found with email: " + email);
                });

        return studentMapper.toResponseDTO(student);
    }

    // ==================== UPDATE (FULL) ====================
    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        logger.info("Updating student with ID: {}", id);

        // Find existing student
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(AppConstants.STUDENT_NOT_FOUND + id));

        // Check if new email belongs to another student
        if (!existingStudent.getEmail().equals(requestDTO.getEmail()) 
                && studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(AppConstants.EMAIL_EXISTS + requestDTO.getEmail());
        }

        // Update all fields
        studentMapper.updateEntityFromDTO(requestDTO, existingStudent);

        // Save updated student
        Student updatedStudent = studentRepository.save(existingStudent);
        logger.info("Student updated successfully with ID: {}", id);

        return studentMapper.toResponseDTO(updatedStudent);
    }

    // ==================== PARTIAL UPDATE (PATCH) ====================
    @Override
    public StudentResponseDTO partialUpdateStudent(Long id, StudentRequestDTO requestDTO) {
        logger.info("Partial update for student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(AppConstants.STUDENT_NOT_FOUND + id));

        // Update only non-null fields
        if (requestDTO.getName() != null && !requestDTO.getName().isBlank()) {
            existingStudent.setName(requestDTO.getName());
        }
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isBlank()) {
            if (!existingStudent.getEmail().equals(requestDTO.getEmail()) 
                    && studentRepository.existsByEmail(requestDTO.getEmail())) {
                throw new DuplicateEmailException(AppConstants.EMAIL_EXISTS + requestDTO.getEmail());
            }
            existingStudent.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getCourse() != null && !requestDTO.getCourse().isBlank()) {
            existingStudent.setCourse(requestDTO.getCourse());
        }
        if (requestDTO.getPhone() != null && !requestDTO.getPhone().isBlank()) {
            existingStudent.setPhone(requestDTO.getPhone());
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        logger.info("Student partially updated with ID: {}", id);

        return studentMapper.toResponseDTO(updatedStudent);
    }

    // ==================== DELETE ====================
    @Override
    public String deleteStudent(Long id) {
        logger.info("Deleting student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(AppConstants.STUDENT_NOT_FOUND + id));

        studentRepository.delete(student);
        logger.info("Student deleted successfully with ID: {}", id);

        return AppConstants.STUDENT_DELETED + " with ID: " + id;
    }

    // ==================== SEARCH BY NAME ====================
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> searchStudentsByName(String name) {
        logger.info("Searching students with name containing: {}", name);

        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        logger.info("Found {} students matching name: {}", students.size(), name);

        return studentMapper.toResponseDTOList(students);
    }

    // ==================== GET BY COURSE ====================
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByCourse(String course) {
        logger.info("Fetching students enrolled in course: {}", course);

        List<Student> students = studentRepository.findByCourse(course);
        logger.info("Found {} students in course: {}", students.size(), course);

        return studentMapper.toResponseDTOList(students);
    }

    // ==================== GET TOTAL COUNT ====================
    @Override
    @Transactional(readOnly = true)
    public long getTotalStudentCount() {
        long count = studentRepository.count();
        logger.info("Total student count: {}", count);
        return count;
    }

    // ==================== CHECK EMAIL EXISTS ====================
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
}