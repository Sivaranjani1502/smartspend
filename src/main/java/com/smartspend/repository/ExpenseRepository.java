package com.smartspend.repository;

import com.smartspend.model.Expense;
import com.smartspend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndCategory(User user, String category);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.category = :category")
    Double sumAmountByUserAndCategory(@Param("user") User user, @Param("category") String category);

    // New finder methods:
    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Expense> findByUserAndAmountBetween(User user, Double min, Double max);
    List<Expense> findByUserAndTitleContainingIgnoreCase(User user, String keyword);
    
    // Combine category & date as a convenience (optional)
    List<Expense> findByUserAndCategoryAndDateBetween(User user, String category, LocalDate start, LocalDate end);
}



