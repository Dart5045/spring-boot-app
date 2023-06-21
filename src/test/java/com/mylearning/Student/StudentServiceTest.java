package com.mylearning.Student;

import com.mylearning.DTO.StudentRegistrationRequest;
import com.mylearning.exception.DuplicateResourceException;
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
            "alex@gmail.com",
            23
        );
        underTest.addStudent(request);


        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
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
        String email = "test1@gmail.com";
        when(studentDAO.existStudentWithEmail(email)).thenReturn(true);

        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "alex",
                "gonzales",
                "alex@gmail.com",
                23
        );
        //When
        assertThatThrownBy(()->underTest.addStudent(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email [%s] already taken"
                        .formatted(email));

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
    void updateStudentById() {

        //Given

        //When

        //Then
    }

    @Test
    void checkEmail() {

        //Given

        //When

        //Then
    }
}