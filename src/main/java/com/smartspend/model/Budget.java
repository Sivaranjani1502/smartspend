package com.smartspend.model;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private Double limitAmount;     // Main budget amount (THIS IS WHAT USER SETS)

    private Double usedAmount = 0.0; // Auto calculated

    private String description;

    private String month;  // "2025-11"

    @ManyToOne
    private User user;
    
    private LocalDate alertSentDate;

    public LocalDate getAlertSentDate() { return alertSentDate; }
    public void setAlertSentDate(LocalDate date) { this.alertSentDate = date; }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getLimitAmount() { return limitAmount; }
    public void setLimitAmount(Double limitAmount) { this.limitAmount = limitAmount; }

    public Double getUsedAmount() { 
        return usedAmount == null ? 0.0 : usedAmount; 
    }
    public void setUsedAmount(Double usedAmount) { this.usedAmount = usedAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

