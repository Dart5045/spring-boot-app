package com.mylearning.DTO;

import com.mylearning.Student.Gender;

import java.util.List;

public record StudentDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Integer age,
        Gender gender,
        List<String> roles,
        String username
) {
}
