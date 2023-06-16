package com.mylearning.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    public void add(Student studentDTO);
    public List<Student> getAllStudents();

    Optional<Student> getStudentById(Integer studentId);

}
