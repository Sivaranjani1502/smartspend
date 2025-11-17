package com.smartspend.service;

import com.smartspend.model.Budget;
import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.BudgetRepository;
import com.smartspend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public void updateUsedAmounts(User user) {

        List<Budget> budgets = budgetRepository.findByUser(user);

        for (Budget budget : budgets) {

            if (budget.getMonth() == null || budget.getCategory() == null) {
                budget.setUsedAmount(0.0);
                budgetRepository.save(budget);
                continue;
            }

            YearMonth budgetMonth = YearMonth.parse(budget.getMonth());

            List<Expense> expenses = expenseRepository
                    .findByUserAndCategory(user, budget.getCategory())
                    .stream()
                    .filter(e -> {
                        if (e.getDate() == null) return false;
                        return YearMonth.from(e.getDate()).equals(budgetMonth);
                    })
                    .toList();

            double used = expenses.stream()
                    .mapToDouble(Expense::getAmount)
                    .sum();

            budget.setUsedAmount(used);
            budgetRepository.save(budget);
        }
    }
    public List<Budget> getBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public void saveBudget(Budget b) {
        budgetRepository.save(b);
    }

}


