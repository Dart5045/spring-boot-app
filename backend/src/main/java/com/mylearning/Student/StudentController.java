package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.Student.Student;
import com.mylearning.Student.StudentService;
import com.mylearning.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private StudentService studentService;
    private JWTUtil jwtUtil;

    public StudentController(StudentService studentService, JWTUtil jwtUtil){
        this.studentService = studentService;
        this.jwtUtil= jwtUtil;
    }

    @GetMapping
    public List<Student> getStudents(  ){
        return studentService.getAllStudents();
    }

    @GetMapping("{id}")
    public Student getStudents(
            @PathVariable("id") Long studentId
    ){
        return studentService.getStudent(studentId);
    }
    @PostMapping
    public ResponseEntity<?> registerStudent(
            @RequestBody StudentRegistrationRequest studentRegistrationRequest
    ){
        studentService.addStudent(studentRegistrationRequest);
        String jwtToken = jwtUtil.issueToken(studentRegistrationRequest.email(), "ROLE_STUDENT");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();

    }

    @DeleteMapping("{id}")
    public void deleteStudent(
            @PathVariable("id") Long id
    ){
        studentService.deleteStudentById(id);
    }

    @PutMapping("/{studentId}")
    public void updateSutdent(
            @PathVariable("studentId") Long studentId,
            @RequestBody StudentUpdateRequest studentUpdateRequest
    ){
        studentService.updateStudentById(studentId, studentUpdateRequest);
    }
}
