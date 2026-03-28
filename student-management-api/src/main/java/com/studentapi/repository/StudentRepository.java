package com.studentapi.repository;

import com.studentapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find by email
    Optional<Student> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find by course
    List<Student> findByCourse(String course);

    // Find by name containing (search)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Custom query - Find students by course
    @Query("SELECT s FROM Student s WHERE s.course = :course ORDER BY s.name")
    List<Student> findStudentsByCourse(@Param("course") String course);
}