package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.dto.AuthResponse;
import com.example.scholarship_backend.dto.ForgotPasswordRequest;
import com.example.scholarship_backend.dto.LoginRequest;
import com.example.scholarship_backend.dto.ResetPasswordRequest;
import com.example.scholarship_backend.dto.UserResponse;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        return userService.logout(authHeader);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<java.util.Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userService.forgotPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
    }
}
