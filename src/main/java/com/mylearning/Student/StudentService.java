package com.mylearning.Student;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public List<Student> getAllStudents(){
        return studentDAO.getAllStudents();
    }

    public Student getStudent(Integer studentId){
        return studentDAO
                .getStudentById(studentId)
                .orElseThrow(()->new IllegalArgumentException("Student with id [%s] not found".formatted(studentId)));
    }
}
