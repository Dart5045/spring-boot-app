package com.mylearning.Student;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class StudentJPADataAccessService  implements StudentDAO{
    private final StudentRepository studentRepository;

    public StudentJPADataAccessService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void insert(Student student) {
        this.studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Integer studentId) {
        return  this.studentRepository.findById(studentId);
    }

    @Override
    public boolean existStudentWithEmail(String email) {
        return this.studentRepository.existsStudentByEmail(email);
    }

    @Override
    public void deleteStudentById(Integer studentId) {
        this.studentRepository.deleteById(studentId);
    }

    @Override
    public boolean existsStudentWithId(Integer studentId) {
        return this.studentRepository.existsStudentWithId(studentId);
    }
}
