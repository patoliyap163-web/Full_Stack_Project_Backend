package com.example.scholarship_backend.service;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.dto.UserResponse;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // REGISTER
    public ApiResponse<UserResponse> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ApiResponse<>(false, "Email already exists", null);
        }
        User savedUser = userRepository.save(user);
        return new ApiResponse<>(
                true,
                "User registered successfully",
                new UserResponse(savedUser)
        );
    }

    // LOGIN
    public ApiResponse<UserResponse> login(User user) {
        var existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }
        if (!existingUser.get().getPassword().equals(user.getPassword())) {
            return new ApiResponse<>(false, "Invalid password", null);
        }
        return new ApiResponse<>(
                true,
                "Login successful",
                new UserResponse(existingUser.get())
        );
    }

    // GET USER BY ID
    public ApiResponse<UserResponse> getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        return new ApiResponse<>(true, "User fetched successfully", new UserResponse(userOptional.get()));
    }
}