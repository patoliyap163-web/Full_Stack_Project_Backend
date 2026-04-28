package com.example.scholarship_backend.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private String token;
    private UserResponse data;

    // No-args constructor
    public AuthResponse() {
    }

    // Constructor with all fields
    public AuthResponse(boolean success, String message, String token, UserResponse data) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.data = data;
    }

    // Constructor without token (for error responses)
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.token = null;
        this.data = null;
    }

    // Getters & Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getData() {
        return data;
    }

    public void setData(UserResponse data) {
        this.data = data;
    }
}
