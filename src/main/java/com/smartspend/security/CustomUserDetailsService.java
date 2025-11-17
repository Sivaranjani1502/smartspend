package com.smartspend.security;

import com.smartspend.model.User;
import com.smartspend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("🔍 Trying to login with email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        System.out.println("✅ User found: " + user.getEmail());
        System.out.println("🔐 Encoded password from DB: " + user.getPassword());
        System.out.println("🔑 Role from DB: " + user.getRole());

        return new CustomUserDetails(user);
    }

}
