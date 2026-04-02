package com.example.scholarship_backend.model;

import jakarta.persistence.*;

@Entity
public class FinancialAid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;
    private Double amount;
    private Double interestRate;

    private String deadline;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String requirements;

    // ✅ NEW: Admin who created this aid
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    public FinancialAid() {}

    // Getters & Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    // ✅ NEW Getter/Setter
    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }
}