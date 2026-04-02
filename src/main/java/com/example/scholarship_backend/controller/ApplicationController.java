package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.model.Application;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.service.ApplicationService;
import com.example.scholarship_backend.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    private final ApplicationService service;
    private final UserService userService;

    public ApplicationController(ApplicationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // ✅ STUDENT: Apply for scholarship/aid
    @PostMapping
    public ResponseEntity<ApiResponse<Application>> create(@RequestBody Application app) {
        try {
            Application created = service.create(app);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Application submitted", created));
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("already have an approved application")) {
                message = "You already have financial support from an approved scholarship or financial aid. You cannot apply for another one.";
            }
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, message, null));
        }
    }

    // ✅ STUDENT: Get their own applications
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Application>>> getByStudent(@PathVariable Long studentId) {

        // 🔥 Fetch User object first
        User student = userService.getEntityById(studentId); // we’ll ensure this exists

        List<Application> apps = service.getByStudent(student);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Applications fetched", apps));
    }

    // ✅ ADMIN: Get all applications
    @GetMapping
    public ResponseEntity<ApiResponse<List<Application>>> getAll() {
        List<Application> apps = service.getAll();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "All applications fetched", apps));
    }

    // ✅ ADMIN: Update status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            @PathVariable Long id,
            @RequestBody Application app) {

        if (!service.existsById(id)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Application not found", null));
        }

        Application updated = service.updateStatus(id, app.getStatus());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Application status updated", updated));
    }

    // ✅ ADMIN: Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        if (!service.existsById(id)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Application not found", null));
        }

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Application deleted", null));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<Map<String, List<Application>>>> getByAdmin(
            @PathVariable Long adminId) {

        Map<String, List<Application>> data = service.getApplicationsByAdmin(adminId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Applications fetched successfully", data));
    }
}