package com.mylearning.Student;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

class StudentJPADataAccessServiceTest {

    private  static final Faker FAKER = new Faker();
    private StudentJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private StudentRepository studentRepository;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentJPADataAccessService(
                studentRepository
        );
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void insertStudent() {

        //Given
        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                "password",
                20,
                Gender.MALE);

        //When
        underTest.insertStudent(student);

        //Then
        verify(studentRepository).save(student);
    }

    @Test
    void getAllStudents() {
        //When
        underTest.getAllStudents();

        //Then
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById() {

        //Given
        long studnetId = 2;

        //When
        underTest.getStudentById(studnetId);

        //Then
        verify(studentRepository).findById(studnetId);
    }

    @Test
    void existStudentWithEmail() {

        //Given
        String email = FAKER.internet().safeEmailAddress();

        //When
        underTest.existStudentWithEmail(email);

        //Then
        verify(studentRepository).existsStudentByEmail(email);
    }

    @Test
    void deleteStudentById() {

        //Given
        long studentId = 2;

        //When
        underTest.deleteStudentById(studentId);

        //Then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void existsStudentWithId() {

        //Given
        long studentId = 2;

        //When
        underTest.existsStudentWithId(studentId);

        //Then
        verify(studentRepository).existsStudentById(studentId);
    }

    @Test
    void updateStudend() {

        //Given
        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                "password"
                , 20,
                Gender.MALE);

        //When
        underTest.updateStudent(student);

        //Then
        verify(studentRepository).save(student);
    }
}