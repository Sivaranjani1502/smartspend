package com.smartspend.controller;

import com.smartspend.repository.BudgetRepository;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.repository.UserRepository;
import com.smartspend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalExpenses", expenseRepository.count());
        model.addAttribute("totalBudgets", budgetRepository.count());
        return "admin_dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin_users";
    }

    // 🔵 ENABLE USER
    @GetMapping("/enable/{id}")
    public String enableUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElse(null);
        if (u != null) {
            u.setEnabled(true);
            userRepository.save(u);
        }
        return "redirect:/admin/users";
    }

    // 🔴 DISABLE USER
    @GetMapping("/disable/{id}")
    public String disableUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElse(null);
        if (u != null) {
            u.setEnabled(false);
            userRepository.save(u);
        }
        return "redirect:/admin/users";
    }
}
