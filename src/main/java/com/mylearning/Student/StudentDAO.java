package com.mylearning.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    public void insert(Student student);
    public List<Student> getAllStudents();

    Optional<Student> getStudentById(Integer studentId);

    public boolean existStudentWithEmail(String email);

    public void deleteStudentById(Integer studentId);

    boolean existsStudentWithId(Integer studentId);
}
