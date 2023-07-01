package com.mylearning.auth;

import com.mylearning.DTO.AuthenticationRequest;
import com.mylearning.DTO.AuthenticationResponse;
import com.mylearning.DTO.StudentDTO;
import com.mylearning.DTO.StudentDTOMapper;
import com.mylearning.Student.Student;
import com.mylearning.Student.StudentDAO;
import com.mylearning.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final StudentDTOMapper studentDTOMapper;
    private final JWTUtil jwtUtil;


    public AuthenticationService(AuthenticationManager authenticationManager, StudentDTOMapper studentDTOMapper, @Qualifier("jdbc") StudentDAO studentDAO, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.studentDTOMapper = studentDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Student student = (Student) authentication.getPrincipal();
        StudentDTO studentDTO = studentDTOMapper.apply(student);
        String token = jwtUtil.issueToken(studentDTO.username(), studentDTO.roles());
        return new AuthenticationResponse(token, studentDTO);
    }
}
