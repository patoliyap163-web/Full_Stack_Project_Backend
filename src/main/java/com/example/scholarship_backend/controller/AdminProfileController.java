package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.model.AdminProfile;
import com.example.scholarship_backend.service.AdminProfileService;
import org.springframework.web.bind.annotation.*;
import com.example.scholarship_backend.dto.ApiResponse;

@RestController
@RequestMapping("/admin-profile")
@CrossOrigin
public class AdminProfileController {

    private final AdminProfileService service;

    public AdminProfileController(AdminProfileService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ApiResponse<AdminProfile> getProfile(@PathVariable Long userId) {

        AdminProfile profile = service.getByUserId(userId);

        return new ApiResponse<>(
                true,
                "Admin profile fetched successfully",
                profile
        );
    }

    @PutMapping("/{userId}")
    public ApiResponse<AdminProfile> updateProfile(@PathVariable Long userId, @RequestBody AdminProfile profile) {

        AdminProfile updated = service.updateProfile(userId, profile);

        return new ApiResponse<>(
                true,
                "Admin profile updated successfully",
                updated
        );
    }
}