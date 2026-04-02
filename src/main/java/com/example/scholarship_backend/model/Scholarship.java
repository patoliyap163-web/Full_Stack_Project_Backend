package com.example.scholarship_backend.model;

import jakarta.persistence.*;

@Entity
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private Double amount;
    private Double gpa;

    private String deadline;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String eligibility;

    @Column(length = 1000)
    private String benefits;

    @Column(length = 1000)
    private String requirements;

    // ✅ NEW: Admin who created this scholarship
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    public Scholarship() {}

    // Getters & Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    // ✅ NEW Getter/Setter
    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }
}