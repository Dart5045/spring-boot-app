package com.mylearning.Student;

import com.mylearning.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudentJDBCDataAccessServiceTest  extends AbstractTestContainers {

    private StudentJDBCDataAccessService underTest;
    private final StudentRowMapper studentRowMapper = new StudentRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new StudentJDBCDataAccessService(
                getJdbcTemplate(),
                studentRowMapper
        );
    }

    @Test
    void getAllStudents() {
        //Given
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                FAKER.internet().safeEmailAddress(),
                20);
         underTest.insertStudent(student);

        //When

        List<Student> students = underTest.getAllStudents();

        //Then
        assertThat(students).isNotEmpty();
    }

    @Test
    void getStudentById() {
        //Given
        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);
        underTest.insertStudent(student);

        Long studentId = underTest.getAllStudents()
                .stream()
                .filter(s->s.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Student> actual = underTest.getStudentById(studentId);


        //Then
        assertThat(actual).isPresent().hasValueSatisfying(s->{
            assertThat(s.getId()).isEqualTo(studentId);
            assertThat(s.getFirstName()).isEqualTo(student.getFirstName());
            assertThat(s.getEmail()).isEqualTo(student.getEmail());
            assertThat(s.getAge()).isEqualTo(student.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectUserById() {

        //Given
        Long studentId = -1L;

        //When
        var actual = underTest.getStudentById(studentId);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertStudent() {

        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);
        underTest.insertStudent(student);

        //When

        //Then
    }

    @Test
    void existStudentWithEmail() {

        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);
        underTest.insertStudent(student);

        //When
        var isPresent = underTest.existStudentWithEmail(email);

        //Then
        assertThat(isPresent).isTrue();
    }

    @Test
    void existsStudentWithEmailReturnsFalseWhenDoesNotExists() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        var isPresent = underTest.existStudentWithEmail(email);

        //Then
        assertThat(isPresent).isFalse();

    }

    @Test
    void existsStudentWithId() {

        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);
        underTest.insertStudent(student);
        Long studentId = underTest.getAllStudents()
                .stream()
                .filter(s->s.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsStudentWithId(studentId);

        //Then
        assertThat(actual).isTrue()  ;
    }

    @Test
    void existsStudentWithIdWillReturnFalseWhenIdNotPresent() {
        //Given
        var studentId = -5L;

        //When
        var actual = underTest.existsStudentWithId(studentId);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteStudentById() {

        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);
        underTest.insertStudent(student);
        Long studentId = underTest.getAllStudents()
                .stream()
                .filter(s->s.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        //When
        underTest.deleteStudentById(studentId);
        var actual = underTest.getStudentById(studentId);

        //Then
        assertThat(actual).isNotPresent();
    }


    @Test
    void updateStudentName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);

        underTest.insertStudent(student);

        Long id = underTest.getAllStudents()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        var newName = "Alex";

        // When age is name
        Student update = new Student();
        update.setId(id);
        update.setFirstName(newName);

        underTest.updateStudend(update);

        // Then
        Optional<Student> actual = underTest.getStudentById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(student.getEmail());
            assertThat(c.getAge()).isEqualTo(student.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);

        underTest.insertStudent(student);

        long id = underTest.getAllStudents()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();;

        // When email is changed
        Student update = new Student();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateStudend(update);

        // Then
        Optional<Student> actual = underTest.getStudentById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail); // change
            assertThat(c.getFirstName()).isEqualTo(student.getFirstName());
            assertThat(c.getAge()).isEqualTo(student.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Student student = new Student(
                FAKER.funnyName().name(),
                FAKER.name().lastName(),
                email,
                20);

        underTest.insertStudent(student);

        long id = underTest.getAllStudents()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When age is changed
        Student update = new Student();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateStudend(update);

        // Then
        Optional<Student> actual = underTest.getStudentById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge); // change
            assertThat(c.getFirstName()).isEqualTo(student.getFirstName());
            assertThat(c.getEmail()).isEqualTo(student.getEmail());
        });
    }
}