package com.smartspend;

import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.service.ExpenseService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void testFilterExpenses() {

        User user = new User();
        user.setId(1L);

        Expense e1 = new Expense();
        e1.setUser(user);
        e1.setCategory("Food");
        e1.setAmount(100.0);
        e1.setDate(LocalDate.of(2025,1,10));

        Expense e2 = new Expense();
        e2.setUser(user);
        e2.setCategory("Travel");
        e2.setAmount(500.0);
        e2.setDate(LocalDate.of(2025,1,12));

        when(expenseRepository.findByUser(user)).thenReturn(List.of(e1, e2));

        List<Expense> result = expenseService.filterExpensesByUser(
                user,
                "Food",   // filter category
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertEquals(1, result.size());     // only Food expected
        assertEquals("Food", result.get(0).getCategory());
    }
}
