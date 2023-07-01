package com.mylearning.Student.journey;

import com.github.javafaker.Faker;
import com.mylearning.DTO.AuthenticationRequest;
import com.mylearning.DTO.AuthenticationResponse;
import static org.assertj.core.api.Assertions.assertThat;

import com.mylearning.DTO.StudentDTO;
import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.Student.Gender;
import com.mylearning.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;
    private static Random RANDOM = new Random();
    private static final String AUTHENTICATION_PATH = "api/v1/auth/login";
    private static final String NEW_STUDENT_PATH = "api/v1/students";

    @Test
    void canLogin() {

        //Given
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName  =  faker.name().lastName();
        String email     = lastName+ "-" + UUID.randomUUID() +"@gamil.com";
        int age          = RANDOM.nextInt(0,100);
        Gender gender    = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        String password  = "password";

        //Create student registration request
        StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                password,
                age,
                gender
        );

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );

        //Unauthorized
        webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        //Send a post request
        webTestClient.post()
                .uri(NEW_STUDENT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(studentRegistrationRequest), StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all students
        //Send a post request
        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();
        String jwtToken = result.getResponseHeaders().get(AUTHORIZATION).get(0);
        StudentDTO studentDTO = result.getResponseBody().studentDTO();
        assertThat(jwtUtil.isTokenValid(jwtToken,studentDTO.username())).isTrue();
        assertThat(studentDTO.email()).isEqualTo(email);
        assertThat(studentDTO.age()).isEqualTo(age);
        assertThat(studentDTO.firstName()).isEqualTo(firstName);
        assertThat(studentDTO.username()).isEqualTo(email);
        assertThat(studentDTO.roles()).isEqualTo(List.of("ROLE_STUDENT"));

        //When

        //Then
    }
}
