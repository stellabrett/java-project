package at.codersbay.java.taskapp.rest.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PasswordEncoderService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void testPasswordEncoder() {
        String rawPassword = "testpassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}
