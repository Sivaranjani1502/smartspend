package com.smartspend;

import com.smartspend.model.User;
import com.smartspend.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testEmailExists() {

        User u = new User();
        u.setName("Test User");
        u.setEmail("test@example.com");
        u.setPassword("pass");
        u.setRole("USER");

        userRepository.save(u);

        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("nope@nope.com"));
    }
}
