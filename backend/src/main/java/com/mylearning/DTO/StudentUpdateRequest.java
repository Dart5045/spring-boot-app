package com.mylearning.DTO;

public record StudentUpdateRequest(
        String firstName,
        String lastName,
        String email,
        Integer age
){
}
