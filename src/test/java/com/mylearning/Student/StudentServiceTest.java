package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.DTO.StudentUpdateRequest;
import com.mylearning.exception.DuplicateResourceException;
import com.mylearning.exception.RequestValidationException;
import com.mylearning.exception.ResourceNotFoundException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentDAO);
    }

    @Test
    void getAllStudents() {
        //When
        underTest.getAllStudents();

        //Then
        verify(studentDAO).getAllStudents();

    }

    @Test
    void getStudent() {

        //Given
        long id = -4L;
        Student student = new Student(
                id,
          "Alex",
          "Gonzales",
          "alext@gmail.com",
          23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        //When
        Student actual = underTest.getStudent(id);

        //Then
        assertThat(actual).isEqualTo(student);
    }

    @Test
    void willThrowWhenGetStudentReturnEmptyOptional() {

        //Given
        long id = -4L;
        when(studentDAO.getStudentById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(()->underTest.getStudent(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with id [%s] not found".formatted(id));
    }

    @Test
    void addStudent() {

        //Given
        String email = "test1@gmail.com";

        //When
        when(studentDAO.existStudentWithEmail(email)).thenReturn(false);

        StudentRegistrationRequest request = new StudentRegistrationRequest(
            "alex",
            "gonzales",
                email,
            23
        );
        underTest.addStudent(request);


        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).insertStudent(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getId()).isNull();
        assertThat(capturedStudent.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedStudent.getEmail()).isEqualTo(request.email());
        assertThat(capturedStudent.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingAStudent() {
        //Given
        String emailAlreadyExits = "test1@gmail.com";
        when(studentDAO.existStudentWithEmail(emailAlreadyExits)).thenReturn(true);

        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "alex",
                "gonzales",
                emailAlreadyExits,
                23
        );
        //When
        assertThatThrownBy(()->underTest.addStudent(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email [%s] already taken"
                        .formatted(emailAlreadyExits));

        //Then
        verify(studentDAO, never()).insertStudent(any());
    }

    @Test
    void deleteStudentById() {

        //Given
        long id = -4L;
        when(studentDAO.existsStudentWithId(id)).thenReturn(true);

        //When
        underTest.deleteStudentById(id);

        //Then
        verify(studentDAO).deleteStudentById(id);
    }

    @Test
    void willThrowWhenDeleteStudentByIdNotExists() {

        //Given
        long id = -4L;
        when(studentDAO.existsStudentWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(()->underTest.deleteStudentById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with Id [%s] not found".formatted(id));

        //Then
        verify(studentDAO, never()).deleteStudentById(id);
    }

    @Test
    void canUpdateAllStudentProperties() {
        //Given
        long id = -4L;
        var emailToChange = "alex2@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                "alex@gmail.com",
                23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));
        when(studentDAO.existStudentWithEmail(emailToChange)).thenReturn(false);

        StudentUpdateRequest request = new StudentUpdateRequest(
                "alex2",
                "gonzales2",
                emailToChange,
                24
        );

        //When
        underTest.updateStudentById(id,request);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).updateStudent(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedStudent.getEmail()).isEqualTo(request.email());
        assertThat(capturedStudent.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateStudentFistName() {
        //Given
        long id = -4L;
        var email = "alex@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                email,
                23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequest request = new StudentUpdateRequest(
                "alexA",
                null,
                null,
                null
        );

        //When
        underTest.updateStudentById(id,request);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).updateStudent(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(student.getLastName());
        assertThat(capturedStudent.getEmail()).isEqualTo(student.getEmail());
        assertThat(capturedStudent.getAge()).isEqualTo(student.getAge());
    }

    @Test
    void canUpdateStudentEmail() {
        //Given
        long id = -4L;
        var emailToChange = "alex2@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                "alex@gmail.com",
                23
        );
        when(studentDAO.existStudentWithEmail(emailToChange)).thenReturn(false);
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequest request = new StudentUpdateRequest(
                null,
                null,
                emailToChange,
                null
        );

        //When
        underTest.updateStudentById(id,request);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).updateStudent(studentArgumentCaptor.capture());


        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(student.getLastName());
        assertThat(capturedStudent.getEmail()).isEqualTo(request.email());
        assertThat(capturedStudent.getAge()).isEqualTo(student.getAge());
    }

    @Test
    void canUpdateStudentAge() {
        //Given
        long id = -4L;
        var email = "alex@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                email,
                23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequest request = new StudentUpdateRequest(
                null,
                null,
                null,
                12
        );

        //When
        underTest.updateStudentById(id,request);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).updateStudent(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(student.getLastName());
        assertThat(capturedStudent.getEmail()).isEqualTo(student.getEmail());
        assertThat(capturedStudent.getAge()).isEqualTo(request.age());
    }
    @Test
    void willThrowWhenTryingToUpdateStudentEmailWhenEmailAlreadyTaken() {
        //Given
        long id = -4L;
        var emailToUpdate = "alex@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                "alexi@gmail.com",
                23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequest request = new StudentUpdateRequest(
                null,
                null,
                emailToUpdate,
                null
        );
        when(studentDAO.existStudentWithEmail(emailToUpdate)).thenReturn(true);


        //When
        assertThatThrownBy(()->underTest.updateStudentById(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email [%s] already taken"
                        .formatted(emailToUpdate));
        //Then
        verify(studentDAO, never()).updateStudent(any());

    }


    @Test
    void willThrowWhenAnyChangesArePresent() {
        //Given
        long id = -4L;
        var email = "alex@gmail.com";
        Student student = new Student(
                id,
                "Alex",
                "Gonzales",
                email,
                23
        );
        when(studentDAO.getStudentById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequest request = new StudentUpdateRequest(
                "Alex",
                "Gonzales",
                email,
                23
        );

        //When
        assertThatThrownBy(()->underTest.updateStudentById(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("No data changes found");
        //Then
        verify(studentDAO, never()).updateStudent(any());
    }

    @Test
    void checkEmail() {
        //Given
        var emailToUpdate = "alex@gmail.com";
        when(studentDAO.existStudentWithEmail(emailToUpdate)).thenReturn(true);


        //Then
        assertThatThrownBy(()->underTest.checkEmail(emailToUpdate))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email [%s] already taken"
                        .formatted(emailToUpdate));
    }
}