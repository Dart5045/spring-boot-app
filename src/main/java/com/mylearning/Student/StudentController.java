package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.Student.Student;
import com.mylearning.Student.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private StudentService studentService;


    public StudentController(StudentService studentService){

        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents(
    ){
        return studentService.getAllStudents();
    }

    @GetMapping("{id}")
    public Student getStudents(
            @PathVariable("id") Integer studentId
    ){
        return studentService.getStudent(studentId);
    }
    @PostMapping
    public void registerStudent(
            @RequestBody StudentRegistrationRequest studentRegistrationRequest
    ){
        studentService.addStudent(studentRegistrationRequest);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(
            @PathVariable("id") Integer id
    ){
        studentService.deleteStudentById(id);
    }
}
