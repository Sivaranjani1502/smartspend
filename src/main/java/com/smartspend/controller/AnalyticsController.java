package com.smartspend.controller;

import com.smartspend.model.User;
import com.smartspend.model.Expense;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.repository.UserRepository;
import com.smartspend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AnalyticsController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/analytics/category")
    public Map<String, Double> byCategory(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        List<Expense> list = expenseRepository.findByUser(user);
        Map<String, Double> map = list.stream()
                .filter(e -> e.getCategory() != null)
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(e -> e.getAmount() == null ? 0 : e.getAmount())));
        return map;
    }

    @GetMapping("/api/analytics/monthly")
    public Map<String, Double> monthlySum(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        List<Expense> list = expenseRepository.findByUser(user);
        Map<String, Double> map = new TreeMap<>();
        list.stream().filter(e -> e.getDate() != null)
            .forEach(e -> {
                YearMonth ym = YearMonth.from(e.getDate());
                String key = ym.toString();
                map.put(key, map.getOrDefault(key, 0.0) + (e.getAmount() == null ? 0.0 : e.getAmount()));
            });
        return map;
    }
}
