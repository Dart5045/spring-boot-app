package com.mylearning.Student;

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
    public void add(Student studentDTO) {
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
}
