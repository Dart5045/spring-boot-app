package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.exception.DuplicateResourceException;
import com.mylearning.exception.ResourceNotFoundException;
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
                .orElseThrow(()->new ResourceNotFoundException("Student with id [%s] not found".formatted(studentId)));
    }

    public void addStudent(StudentRegistrationRequest studentRegistrationRequest ){
        String  email = studentRegistrationRequest.email();
        if(this.studentDAO.existStudentWithEmail(email)){
            throw new DuplicateResourceException("Email [%s] already taken"
                    .formatted(email));
        }
        Student student = new Student(
                null,
                studentRegistrationRequest.firstName(),
                studentRegistrationRequest.lastName(),
                studentRegistrationRequest.email(),
                studentRegistrationRequest.age()
        );
        this.studentDAO.insert(student);
    }

    public void deleteStudentById(Integer studentId)
    {
        if(!this.studentDAO.existsStudentWithId(studentId)){
            throw new ResourceNotFoundException("Student with Id [%s] not found".formatted(studentId));
        }
        this.studentDAO.deleteStudentById(studentId);
    }
}
