package com.smartspend;

import com.smartspend.model.Budget;
import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.BudgetRepository;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.service.BudgetService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void testBudgetUpdate() {

        User user = new User();
        user.setId(1L);

        Budget budget = new Budget();
        budget.setCategory("Food");
        budget.setLimitAmount(1000.0);
        budget.setUsedAmount(0.0);

        Expense e1 = new Expense();
        e1.setCategory("Food");
        e1.setAmount(200.0);

        Expense e2 = new Expense();
        e2.setCategory("Food");
        e2.setAmount(300.0);

        when(budgetRepository.findByUser(user)).thenReturn(List.of(budget));
        when(expenseRepository.findByUser(user)).thenReturn(List.of(e1, e2));

        budgetService.updateUsedAmounts(user);

        assertEquals(500.0, budget.getUsedAmount());
    }
}
