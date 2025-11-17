package com.smartspend.controller;

import com.smartspend.model.User;
import com.smartspend.repository.UserRepository;
import com.smartspend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ---------------------------
    // FORGOT PASSWORD PAGE
    // ---------------------------
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot_password";
    }

    // ---------------------------
    // PROCESS FORGOT PASSWORD
    // ---------------------------
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        Model model) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "No account found with this email.");
            return "forgot_password";
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        emailService.sendEmail(
                email,
                "SmartSpend Password Reset",
                "Click the link to reset your password:\n\n" + resetLink
        );

        model.addAttribute("msg", "A password reset link has been sent to your email.");
        return "forgot_password";
    }

    // ---------------------------
    // SHOW RESET PASSWORD PAGE
    // ---------------------------
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token,
                                        Model model) {

        User user = userRepository.findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset_password";
        }

        model.addAttribute("token", token);
        return "reset_password";
    }

    // ---------------------------
    // PROCESS NEW PASSWORD
    // ---------------------------
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {

        User user = userRepository.findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset_password";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setTokenExpiry(null);

        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }
}
