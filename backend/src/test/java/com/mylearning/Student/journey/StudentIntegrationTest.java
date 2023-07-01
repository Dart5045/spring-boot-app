package com.mylearning.Student.journey;

import com.github.javafaker.Faker;
import com.mylearning.DTO.StudentDTO;
import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.Student.Gender;
import com.mylearning.Student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static Random RANDOM = new Random();
    private static final String STUDENT_URI = "api/v1/students";


    @Test
    void canRegisterStudent() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName  =  faker.name().lastName();
        String email     = lastName+ "-" + UUID.randomUUID() +"@gamil.com";
        int age          = RANDOM.nextInt(0,100);
        Gender gender    = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                "password",
                age,
                gender
        );

        //Send a post request
        String jwtToken = webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //Get all students
        List<StudentDTO> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<StudentDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present
        long id = allStudents
                .stream()
                .filter(student -> student.email().equals(email))
                .map(StudentDTO::id)
                .findFirst()
                .orElseThrow();

        StudentDTO expectedStudentDTO = new StudentDTO(
                id,
                firstName,
                lastName,
                email,
                age,
                gender,
                List.of("ROLE_STUDENT"),
                email
        );

        assertThat(allStudents)
                .contains(expectedStudentDTO);


        //Get student by id
        webTestClient.get()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<StudentDTO>() { })
                .isEqualTo(expectedStudentDTO)
        ;
    }

    @Test
    void canDeleteStudent() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName  =  faker.name().lastName();
        String email     = lastName+ "-" + UUID.randomUUID() +"@gamil.com";
        int age          = RANDOM.nextInt(0,100);
        Gender gender    = age % 2 == 0?Gender.MALE:Gender.FEMALE;


        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,  lastName,  email,  "password",  age,   gender
        );

        StudentRegistrationRequest request2 = new StudentRegistrationRequest(
                firstName,  lastName,  email+"bo",  "password",  age,   gender
        );

        //Send a post request to create student 1
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Send a post request to create student 2
        String jwtToken = webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //Get all students
        List<StudentDTO> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<StudentDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present

        long id = allStudents
                .stream()
                .filter(studentDTO -> studentDTO.email().equals(email))
                .map(StudentDTO::id)
                .findFirst()
                .orElseThrow();

        //Delete student by id
        webTestClient.delete()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk();

        //Get student by id
        webTestClient.get()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateStudent() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName  =  faker.name().lastName();
        String email     = lastName+ "-" + UUID.randomUUID() +"@gamil.com";
        int age          = RANDOM.nextInt(0,100);
        Gender gender    = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                "password",
                age,
                gender
        );


        //Send a post request
        String jwtToken = webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //Get all students
        List<StudentDTO> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<StudentDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present
        long id = allStudents
                .stream()
                .filter(student -> student.email().equals(email))
                .map(StudentDTO::id)
                .findFirst()
                .orElseThrow();

        //Update student
        String newName = "Alex";
        StudentUpdateRequest updateRequest = new StudentUpdateRequest(
                newName, null,null,null,null
        );

        webTestClient.put()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),StudentUpdateRequest.class)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk();


        //Get student by id
        StudentDTO studentDTOUpdated = webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<StudentDTO>() {
                })
                .returnResult()
                .getResponseBody();

        StudentDTO expectedStudentDTO = new StudentDTO(
            id,
            newName,
            lastName,
            email,
            age,
            gender,
            List.of("ROLE_STUDENT"),
            email
        );
        assertThat(expectedStudentDTO).isEqualTo(studentDTOUpdated);
    }
}
