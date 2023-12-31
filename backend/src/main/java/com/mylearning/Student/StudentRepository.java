package com.mylearning.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    boolean existsStudentByEmail(String email);
    boolean existsStudentById(Long studentId);

    Optional<Student> findStudentByEmail(String email);
}
