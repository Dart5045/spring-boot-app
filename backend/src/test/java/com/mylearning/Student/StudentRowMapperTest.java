package com.mylearning.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


class StudentRowMapperTest {

    private StudentRowMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentRowMapper();
    }

    @Test
    void mapRow() throws SQLException {

        //Given
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(3L);
        when(resultSet.getString("first_name")).thenReturn("Alex");
        when(resultSet.getString("last_name")).thenReturn("Gonzales");
        when(resultSet.getString("email")).thenReturn("alex@gmail.com");
        when(resultSet.getInt("age")).thenReturn(20);

        //When
        Student student = underTest.mapRow(resultSet, 1);


        //Then
        Student expected = new Student(
                3L,
                "Alex",
                "Gonzales",
                "alex@gmail.com",
                "password",
                20,
                Gender.MALE
        );
        assertThat(student).isEqualTo(expected);
    }
}