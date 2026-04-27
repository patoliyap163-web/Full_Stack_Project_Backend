package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.model.Scholarship;
import com.example.scholarship_backend.service.ScholarshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "https://fullstackproject-nu.vercel.app")
public class ScholarshipController {

    private final UserService userService;
    private final ScholarshipService service;

    public ScholarshipController(UserService userService, ScholarshipService service) {
        this.userService = userService;
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<Scholarship>> create(@RequestBody Scholarship s) {
        Scholarship saved = service.create(s);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Scholarship created successfully", saved));
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<Scholarship>>> getAll() {
        List<Scholarship> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Scholarships fetched successfully", list));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Scholarship>> update(
            @PathVariable Long id,
            @RequestBody Scholarship s) {

        if (!service.existsById(id)) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(false, "Scholarship with id " + id + " not found", null));
        }

        Scholarship updated = service.update(id, s);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Scholarship updated successfully", updated));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScholarship(@PathVariable Long id) {

        if (!service.existsById(id)) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(false, "Scholarship with id " + id + " not found", null));
        }

        try {
            service.delete(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Scholarship deleted successfully", null));
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Cannot delete this scholarship")) {
                message = "This scholarship cannot be deleted because it has already been approved for one or more students. Deleting approved scholarships would compromise data integrity and student records.";
            }
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, message, null));
        }
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<List<Scholarship>>> getByAdmin(@PathVariable Long adminId) {

        User admin = userService.getEntityById(adminId);

        List<Scholarship> list = service.getByAdmin(admin);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Admin scholarships fetched", list));
    }
}