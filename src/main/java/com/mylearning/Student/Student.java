package com.mylearning.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    private  Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
}
