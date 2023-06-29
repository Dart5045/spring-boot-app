package com.mylearning.DTO;

import com.mylearning.Student.Gender;

public record StudentRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Integer age,

        Gender gender
){
}
