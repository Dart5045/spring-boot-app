package com.mylearning.controller;

import com.mylearning.Student.Student;
import com.mylearning.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getStudents(
    ){
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public Student getStudents(
            @PathVariable("id") Integer studentId
    ){
        return studentService.getStudent(studentId);
    }
}
