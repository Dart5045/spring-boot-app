package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    public void insertStudent(Student student);
    public List<Student> getAllStudents();

    Optional<Student> getStudentById(Long studentId);

    public boolean existStudentWithEmail(String email);

    public void deleteStudentById(Long studentId);

    boolean existsStudentWithId(Long studentId);

    void updateStudend(Student student);
}
