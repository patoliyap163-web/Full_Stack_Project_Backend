package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.model.StudentProfile;
import com.example.scholarship_backend.service.StudentProfileService;
import org.springframework.web.bind.annotation.*;
import com.example.scholarship_backend.dto.ApiResponse;

@RestController
@RequestMapping("/student-profile")
@CrossOrigin(origins = "https://fullstackproject-nu.vercel.app")
public class StudentProfileController {

    private final StudentProfileService service;

    public StudentProfileController(StudentProfileService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ApiResponse<StudentProfile> getProfile(@PathVariable Long userId) {

        StudentProfile profile = service.getByUserId(userId);

        return new ApiResponse<>(
                true,
                "Profile fetched successfully",
                profile);
    }

    @PutMapping("/{userId}")
    public ApiResponse<StudentProfile> updateProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfile updatedData) {

        StudentProfile profile = service.updateProfile(userId, updatedData);

        return new ApiResponse<>(
                true,
                "Profile updated successfully",
                profile
        );
    }
}