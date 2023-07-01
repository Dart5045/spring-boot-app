package com.mylearning.DTO;

public record AuthenticationRequest(
        String username,
        String password
) {
}
