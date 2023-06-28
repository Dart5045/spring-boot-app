package com.mylearning.DTO;

import com.mylearning.Student.Gender;

public record StudentUpdateRequest(
        String firstName,
        String lastName,
        String email,
        Integer age,
        Gender gender
){
}
