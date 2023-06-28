package com.mylearning.Student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class StudentJDBCDataAccessService implements StudentDAO{
    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;


    public StudentJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                        StudentRowMapper studentRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public void insertStudent(Student student) {

        var sql  = """
                INSERT INTO student(
                first_name, last_name, email, age, gender
                ) VALUES( ? ,? , ? ,?, ? )
                """;
        int update = jdbcTemplate.update(sql,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge(),
                student.getGender().name());
    }

    @Override
    public List<Student> getAllStudents() {
        final String SQL_FIND_ALL_STUDENTS = """
                SELECT id, first_name, last_name, email, age, gender 
                FROM student""";
        return jdbcTemplate.query(SQL_FIND_ALL_STUDENTS,studentRowMapper);
    }

    @Override
    public Optional<Student> getStudentById(Long studentId) {
        final String SQL_FIND_STUDENT_BY_ID = """
                SELECT id, first_name, last_name, email, age, gender
                FROM student WHERE id= ? """;
        Optional<Student> student = jdbcTemplate.query(SQL_FIND_STUDENT_BY_ID,studentRowMapper, studentId)
                .stream()
                .findFirst();

        return student;
    }

    @Override
    public boolean existStudentWithEmail(String email) {
        final String SQL_FIND_STUDENT_BY_EMAIL = """
                SELECT count(id) 
                FROM student WHERE email= ? """;
        Integer count =  jdbcTemplate.queryForObject(SQL_FIND_STUDENT_BY_EMAIL,Integer.class, email);
        return count!=null && count>0;
    }

    @Override
    public void deleteStudentById(Long studentId) {
        final String SQL_DELETE_STUDENT_BY_ID = """
                DELETE FROM student WHERE id= ? """;
        int update = jdbcTemplate.update(SQL_DELETE_STUDENT_BY_ID,studentId);
    }

    @Override
    public boolean existsStudentWithId(Long studentId) {
        final String SQL_FIND_STUDENT_BY_EMAIL = """
                SELECT count(id) 
                FROM student WHERE id= ? """;
        Integer count =  jdbcTemplate.queryForObject(SQL_FIND_STUDENT_BY_EMAIL,Integer.class, studentId);
        return count!=null && count>0;


    }

    @Override
    public void updateStudent(Student student) {
        if(student.getFirstName()!= null){
            String sql ="""
                UPDATE student SET
                first_name = ?
                WHERE id=? """;
            int update = jdbcTemplate.update(sql, student.getFirstName(), student.getId());
        }

        if(student.getLastName()!= null){
            String sql ="""
                UPDATE student SET
                last_name = ?
                WHERE id=? """;
            int update = jdbcTemplate.update(sql, student.getLastName(), student.getId());
        }

        if(student.getEmail()!= null){
            String sql ="""
                UPDATE student SET
                email = ?
                WHERE id=? """;
            int update = jdbcTemplate.update(sql, student.getEmail(), student.getId());
        }

        if(student.getAge()!= null){
            String sql ="""
                UPDATE student SET
                age = ?
                WHERE id=? """;
            int update = jdbcTemplate.update(sql, student.getAge(), student.getId());
        }
    }
}
