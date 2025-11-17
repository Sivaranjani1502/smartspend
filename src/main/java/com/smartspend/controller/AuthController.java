package com.smartspend.controller;

import com.smartspend.model.User;
import com.smartspend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

 // ENABLE USER
    @GetMapping("/user/enable/{id}")
    public String enableUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setEnabled(true);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    // DISABLE USER
    @GetMapping("/user/disable/{id}")
    public String disableUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setEnabled(false);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // store roles EXACTLY as you said:
        user.setRole("USER");     // for admin you manually insert in DB
        // new normal users get:
        // user.setRole("user");

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
