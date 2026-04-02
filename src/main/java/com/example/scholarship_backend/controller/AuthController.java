package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.dto.UserResponse;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins ="http://localhost:3000")
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
    public ApiResponse<UserResponse> login(@RequestBody User user) {
        return userService.login(user);
    }
}