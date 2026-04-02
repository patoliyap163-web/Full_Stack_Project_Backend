package com.example.scholarship_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Link to Student (User)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // ✅ Link to Scholarship (optional)
    @ManyToOne
    @JoinColumn(name = "scholarship_id")
    private Scholarship scholarship;

    // ✅ Link to Financial Aid (optional)
    @ManyToOne
    @JoinColumn(name = "financial_aid_id")
    private FinancialAid financialAid;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime appliedAt = LocalDateTime.now();

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public Application() {}

    // ✅ Getters & Setters

    public Long getId() { return id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Scholarship getScholarship() { return scholarship; }
    public void setScholarship(Scholarship scholarship) { this.scholarship = scholarship; }

    public FinancialAid getFinancialAid() { return financialAid; }
    public void setFinancialAid(FinancialAid financialAid) { this.financialAid = financialAid; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}