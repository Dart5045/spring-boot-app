package com.mylearning.Student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class StudentJDBCDataAccessService implements StudentDAO{
    private final JdbcTemplate jdbcTemplate;

    public StudentJDBCDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void insert(Student student) {
        var sql  = """
                INSERT INTO student(
                first_name, last_name, email, age
                ) VALUES( ? ,? , ? ,? )
                """;
        int update = jdbcTemplate.update(sql,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge());
    }

    @Override
    public List<Student> getAllStudents() {
        final String SQL_FIND_STUDENT = """
                SELECT id, first_name, last_name, email, age 
                FROM student""";
        RowMapper<Student> studentRowMapper =  (rs, rowNum) -> {
            Student student = new Student(
                    rs.getLong("id") ,
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getInt("age")
            );
            return student;
            };
        List<Student> studentList= jdbcTemplate.query(SQL_FIND_STUDENT,studentRowMapper);
        return  studentList;
    }

    @Override
    public Optional<Student> getStudentById(Integer studentId) {
        return Optional.empty();
    }

    @Override
    public boolean existStudentWithEmail(String email) {
        return false;
    }

    @Override
    public void deleteStudentById(Integer studentId) {

    }

    @Override
    public boolean existsStudentWithId(Integer studentId) {
        return false;
    }

    @Override
    public void save(Student student) {

    }
}
