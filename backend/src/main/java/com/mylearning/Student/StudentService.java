package com.mylearning.Student;

import com.mylearning.DTO.StudentDTO;
import com.mylearning.DTO.StudentDTOMapper;
import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.exception.DuplicateResourceException;
import com.mylearning.exception.RequestValidationException;
import com.mylearning.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/*
  business logic
  */
@Service
public class StudentService {

    private final StudentDAO studentDAO;
    private final PasswordEncoder passwordEncoder;
    private final StudentDTOMapper studentDTOMapper;

    public StudentService(@Qualifier("jdbc") StudentDAO studentDAO, PasswordEncoder passwordEncoder, StudentDTOMapper studentDTOMapper) {
        this.studentDAO = studentDAO;
        this.passwordEncoder = passwordEncoder;
        this.studentDTOMapper = studentDTOMapper;
    }

    public List<StudentDTO> getAllStudents(){

        return studentDAO
                .getAllStudents()
                .stream()
                .map(student -> studentDTOMapper.apply(student))
                .collect(Collectors.toList());
    }

    public StudentDTO getStudent(Long studentId){
        return studentDAO
                .getStudentById(studentId)
                .map(student->studentDTOMapper.apply(student))
                .orElseThrow(()->new ResourceNotFoundException("Student with id [%s] not found".formatted(studentId)));
    }

    public void addStudent(StudentRegistrationRequest studentRegistrationRequest ){
        String  email = studentRegistrationRequest.email();
        checkEmail(email);

        String password = passwordEncoder.encode(studentRegistrationRequest.password());

        Student student = new Student(
                studentRegistrationRequest.firstName(),
                studentRegistrationRequest.lastName(),
                studentRegistrationRequest.email(),
                password,
                studentRegistrationRequest.age(),
                studentRegistrationRequest.gender()
        );

        this.studentDAO.insertStudent(student);
    }

    public void deleteStudentById(Long studentId)
    {
        if(!this.studentDAO.existsStudentWithId(studentId)){
            throw new ResourceNotFoundException("Student with Id [%s] not found".formatted(studentId));
        }
        this.studentDAO.deleteStudentById(studentId);
    }

    public void updateStudentById(Long studentId,
                                  StudentUpdateRequest updateRequest) {
         Student student =  studentDAO
                .getStudentById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student with id [%s] not found".formatted(studentId)));


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
        if(updateRequest.age()!=null && !updateRequest.age().equals(student.getAge()))
        {
            changes = true;
            student.setAge(updateRequest.age());
        }

         if(!changes){
             throw new RequestValidationException("No data changes found");
         }
         this.studentDAO.updateStudent(student);
    }

    public void checkEmail(String email){
        if(this.studentDAO.existStudentWithEmail(email)){
            throw new DuplicateResourceException("Email [%s] already taken"
                    .formatted(email));
        }
    }
}
