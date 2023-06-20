package com.mylearning.Student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
    boolean existsStudentByEmail(String email);
    boolean existsStudentById(Long studentId);

}
