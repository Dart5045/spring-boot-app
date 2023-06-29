package com.mylearning.Student;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentUserDetailService implements UserDetailsService {

    private final StudentDAO studentDAO;

    public StudentUserDetailService(@Qualifier("jpa") StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return studentDAO.selectStudentByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Username "+ username +" not found")
        );
    }
}
