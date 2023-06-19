package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.exception.DuplicateResourceException;
import com.mylearning.exception.RequestValidationException;
import com.mylearning.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/*
  business logic
  */
@Service
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(@Qualifier("jdbc") StudentDAO studentDAO) {
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
        this.checkEmail(email);

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

    public void updateStudentById(Integer studentId,
                                  StudentUpdateRequest updateRequest) {
         Student student =  getStudent(studentId);
         boolean changes = false;
         if(updateRequest.firstName()!=null && !updateRequest.firstName().equals(student.getFirstName()))
         {
             changes = true;
             student.setFirstName(updateRequest.firstName());
         }

         if(updateRequest.lastName()!=null && !updateRequest.lastName().equals(student.getLastName()))
         {
             changes = true;
            student.setLastName(updateRequest.lastName());
         }

        if(updateRequest.email()!=null && !updateRequest.email().equals(student.getEmail()))
        {
            this.checkEmail(updateRequest.email());

            changes = true;
            student.setEmail(updateRequest.email());
        }

         if(!changes){
             throw new RequestValidationException("No data changes found");
         }
         this.studentDAO.updateStudend(student);
    }

    public void checkEmail(String email){
        if(this.studentDAO.existStudentWithEmail(email)){
            throw new DuplicateResourceException("Email [%s] already taken"
                    .formatted(email));
        }
    }
}
