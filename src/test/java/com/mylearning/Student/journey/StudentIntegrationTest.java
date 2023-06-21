package com.mylearning.Student.journey;

import com.github.javafaker.Faker;
import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.Student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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

        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                age
        );

        //Send a post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all students
        List<Student> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present
        Student expectedStudent = new Student(
            firstName,
            lastName,
            email,
            age
        );
        assertThat(allStudents)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedStudent);


        long id = allStudents
                .stream()
                .filter(student -> student.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        expectedStudent.setId(id);
        //Get student by id
        webTestClient.get()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Student>() { })
                .isEqualTo(expectedStudent);
    }

    @Test
    void canDeeteStudent() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName  =  faker.name().lastName();
        String email     = lastName+ "-" + UUID.randomUUID() +"@gamil.com";
        int age          = RANDOM.nextInt(0,100);

        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                age
        );

        //Send a post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all students
        List<Student> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present

        long id = allStudents
                .stream()
                .filter(student -> student.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        //Delete student by id
        webTestClient.delete()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        //Get student by id
        webTestClient.get()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
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

        //Create student registration request
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                firstName,
                lastName,
                email,
                age
        );


        //Send a post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),StudentRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all students
        List<Student> allStudents = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //Make sure student is present




        long id = allStudents
                .stream()
                .filter(student -> student.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();


        //Update student
        String newEmail = "2"+email;
        StudentUpdateRequest updateRequest = new StudentUpdateRequest(
                "2"+firstName,
                "2"+lastName,
                newEmail,
                2+age
        );
        Student expectedStudent = new Student(
                id,
                "2"+firstName,
                "2"+lastName,
                newEmail,
                2+age
        );

        webTestClient.put()
                .uri(STUDENT_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),StudentUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //Get student
        Student studentUpdated = webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(expectedStudent).isEqualTo(studentUpdated);
    }
}
