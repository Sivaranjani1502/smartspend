package com.smartspend.controller;

import com.smartspend.model.Budget;
import com.smartspend.model.User;
import com.smartspend.repository.BudgetRepository;
import com.smartspend.repository.UserRepository;
import com.smartspend.service.BudgetService;
import com.smartspend.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ExpenseService expenseService;

    // LIST BUDGETS
    @GetMapping
    public String listBudgets(Model model, Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        budgetService.updateUsedAmounts(user);

        List<Budget> budgets = budgetRepository.findByUser(user);
        model.addAttribute("budgets", budgets);

        return "budget";
    }

    // SHOW FORM
    @GetMapping({"/new", "/edit/{id}"})
    public String showBudgetForm(@PathVariable(required = false) Long id,
                                 Model model,
                                 Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Budget budget = (id != null)
                ? budgetRepository.findById(id).orElse(new Budget())
                : new Budget();

        model.addAttribute("budget", budget);
        model.addAttribute("categories", expenseService.getFixedCategories());

        model.addAttribute("months", List.of(
                YearMonth.now().minusMonths(1).toString(),
                YearMonth.now().toString(),
                YearMonth.now().plusMonths(1).toString()
        ));

        return "budget_form";
    }

    // SAVE
    @PostMapping
    public String saveBudget(@ModelAttribute Budget budget, Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        budget.setUser(user);
        budgetRepository.save(budget);

        return "redirect:/budget";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id, Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Budget budget = budgetRepository.findById(id).orElseThrow();

        if (budget.getUser().getId().equals(user.getId())) {
            budgetRepository.delete(budget);
        }

        return "redirect:/budget";
    }
}

