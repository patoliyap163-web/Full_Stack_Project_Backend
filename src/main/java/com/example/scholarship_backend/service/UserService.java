package com.example.scholarship_backend.service;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.dto.AuthResponse;
import com.example.scholarship_backend.dto.UserResponse;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.repository.UserRepository;
import com.example.scholarship_backend.security.RevokedTokenService;
import com.example.scholarship_backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RevokedTokenService revokedTokenService;

    @Autowired
    private PasswordResetCodeService passwordResetCodeService;

    @Autowired
    private EmailService emailService;

    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // REGISTER with password encoding
    public ApiResponse<UserResponse> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ApiResponse<>(false, "Email already exists", null);
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("student");
        }

        User savedUser = userRepository.save(user);
        return new ApiResponse<>(
                true,
                "User registered successfully",
                new UserResponse(savedUser));
    }

    // LOGIN with JWT token generation
    public AuthResponse login(String email, String password) {
        var existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            return new AuthResponse(false, "User not found");
        }

        User user = existingUser.get();

        // Compare passwords (encoded vs plain)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponse(false, "Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return new AuthResponse(
                true,
                "Login successful",
                token,
                new UserResponse(user));
    }

    public ApiResponse<Void> logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponse<>(false, "Authorization header with Bearer token is required for logout.");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            return new ApiResponse<>(false, "Token is invalid or already expired.");
        }

        revokedTokenService.revokeToken(token, jwtUtil.extractExpiration(token));
        return new ApiResponse<>(true, "Logout successful. Token revoked. Remove the JWT from local storage on the client side.");
    }

    public ApiResponse<Map<String, String>> forgotPassword(String email) {
        if (email == null || email.isBlank()) {
            return new ApiResponse<>(false, "Email is required", null);
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        String code = passwordResetCodeService.generateCodeForEmail(email);

        try {
            emailService.sendPasswordResetCode(email, code);
        } catch (Exception e) {
            logger.error("Failed to send password reset code to {}", email, e);
            Map<String, String> data = new HashMap<>();
            data.put("code", code);
            return new ApiResponse<>(true, "Verification code generated, but email delivery failed. Use the returned code for development.", data);
        }

        Map<String, String> data = new HashMap<>();
        data.put("code", code);
        return new ApiResponse<>(true, "Verification code sent", data);
    }

    public ApiResponse<Void> resetPassword(String email, String code, String newPassword) {
        if (email == null || email.isBlank()) {
            return new ApiResponse<>(false, "Email is required");
        }

        if (code == null || code.isBlank()) {
            return new ApiResponse<>(false, "Reset code is required");
        }

        if (newPassword == null || newPassword.isBlank()) {
            return new ApiResponse<>(false, "New password is required");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return new ApiResponse<>(false, "User not found");
        }

        PasswordResetCodeService.ResetCodeValidationResult validationResult =
                passwordResetCodeService.validateCode(email, code);

        if (validationResult == PasswordResetCodeService.ResetCodeValidationResult.EXPIRED) {
            return new ApiResponse<>(false, "Reset code has expired");
        }

        if (validationResult == PasswordResetCodeService.ResetCodeValidationResult.INVALID) {
            return new ApiResponse<>(false, "Invalid reset code");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetCodeService.invalidateCode(email);

        return new ApiResponse<>(true, "Password reset successful");
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
