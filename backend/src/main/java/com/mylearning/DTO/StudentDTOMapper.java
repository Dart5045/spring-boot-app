package com.mylearning.DTO;

import com.mylearning.Student.Student;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentDTOMapper implements Function<Student,StudentDTO> {


    @Override
    public StudentDTO apply(Student student) {
        return new StudentDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge(),
                student.getGender(),
                student.getAuthorities()
                        .stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.toList()),
                student.getUsername());
    }
}
