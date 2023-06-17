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
    public void add(Student studentDTO) {

    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Integer studentId) {
        return Optional.empty();
    }
}
