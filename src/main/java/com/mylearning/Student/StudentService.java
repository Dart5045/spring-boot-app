package com.mylearning.Student;

import com.mylearning.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/*
  bussiness logic
  */
@Service
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(@Qualifier("jpa") StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public List<Student> getAllStudents(){
        return studentDAO.getAllStudents();
    }

    public Student getStudent(Integer studentId){
        return studentDAO
                .getStudentById(studentId)
                .orElseThrow(()->new ResourceNotFound("Student with id [%s] not found".formatted(studentId)));
    }
}
