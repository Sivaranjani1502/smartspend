package com.smartspend.service;

import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> filterExpensesByUser(
            User user,
            String category,
            String type,
            LocalDate startDate,
            LocalDate endDate,
            Double minAmount,
            Double maxAmount,
            String searchTitle) {

        List<Expense> list = expenseRepository.findByUser(user);

        if (category != null && !category.isEmpty())
            list = list.stream()
                .filter(e -> category.equalsIgnoreCase(e.getCategory()))
                .collect(Collectors.toList());

        if (type != null && !type.isEmpty())
            list = list.stream()
                .filter(e -> type.equalsIgnoreCase(e.getType()))
                .collect(Collectors.toList());

        if (startDate != null && endDate != null)
            list = list.stream()
                .filter(e -> e.getDate() != null
                        && !e.getDate().isBefore(startDate)
                        && !e.getDate().isAfter(endDate))
                .collect(Collectors.toList());

        if (minAmount != null && maxAmount != null)
            list = list.stream()
                .filter(e -> e.getAmount() >= minAmount && e.getAmount() <= maxAmount)
                .collect(Collectors.toList());

        if (searchTitle != null && !searchTitle.isEmpty())
            list = list.stream()
                .filter(e -> e.getTitle() != null &&
                             e.getTitle().toLowerCase().contains(searchTitle.toLowerCase()))
                .collect(Collectors.toList());

        return list;
    }

    public List<String> getFixedCategories() {
        return List.of(
                "Food & Drinks", "Groceries", "Dining Out / Restaurants",
                "Coffee / Snacks", "Transportation", "Fuel / Gas",
                "Public Transport", "Taxi / Ride-sharing", "Housing / Bills",
                "Rent / Mortgage", "Utilities", "Internet / Phone",
                "Doctor / Medical", "Medicines", "Gym / Fitness",
                "Hobbies", "Entertainment", "Travel",
                "Education", "Loans / EMIs", "Savings / Investments",
                "Others / Miscellaneous"
        );
    }
}

