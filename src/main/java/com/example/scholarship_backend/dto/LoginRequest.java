package com.example.scholarship_backend.dto;

public class LoginRequest {

    private String email;
    private String password;

    // No-args constructor
    public LoginRequest() {
    }

    // Constructor with fields
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
