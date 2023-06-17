package com.mylearning.Student;

import com.mylearning.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class StudentListDataAccessService implements StudentDAO{


    private static List<Student> studentList;

    static {
        studentList = new ArrayList<>();
        Student alex = new Student(1,"Alex","Gonzales","alex@gmail.com",12 );
        Student adalid = new Student(1,"Adalid","Gonzales","adalid@gmail.com",12 );
        studentList.add(alex);
        studentList.add(adalid);
    }

    @Override
    public void insert(Student studentDTO) {
        Student student = new Student();

    }

    @Override
    public List<Student> getAllStudents() {
        return this.studentList;
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return  this.studentList.
                stream()
                .filter(student->student.getId().equals( id ))
                .findFirst();
    }

    @Override
    public boolean existStudentWithEmail(String email) {
        return this.studentList
                .stream()
                .anyMatch(student->student.getEmail().equals(email));
    }

    @Override
    public void deleteStudentById(Integer studentId) {
        studentList.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .ifPresent(studentList::remove);
    }

    @Override
    public boolean existsStudentWithId(Integer studentId) {
        return studentList.stream().anyMatch(
                student -> student.getId().equals(studentId)
        );
    }

    @Override
    public void save(Student student) {
        studentList.stream()
                .filter(s -> s.getId().equals(student.getId()))
                .findFirst()
                .ifPresent(studentList::remove);
    }
}