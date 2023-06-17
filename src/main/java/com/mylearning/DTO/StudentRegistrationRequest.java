package com.mylearning.DTO;

public record StudentRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Integer age
){
}
