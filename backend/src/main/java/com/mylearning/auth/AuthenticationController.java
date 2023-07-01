package com.mylearning.auth;

import com.mylearning.DTO.AuthenticationRequest;
import com.mylearning.DTO.AuthenticationResponse;
import com.mylearning.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private JWTUtil jwtUtil;

    public AuthenticationController(AuthenticationService authenticationService, JWTUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request)
    {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .build();
    }
}
