package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.dto.UserResponse;
import com.example.scholarship_backend.model.FinancialAid;
import com.example.scholarship_backend.model.Scholarship;
import com.example.scholarship_backend.model.Application;
import com.example.scholarship_backend.service.FinancialAidService;
import com.example.scholarship_backend.service.ScholarshipService;
import com.example.scholarship_backend.service.UserService;
import com.example.scholarship_backend.service.ApplicationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dashboard")
public class AdminDashboardController {

    private final ScholarshipService scholarshipService;
    private final FinancialAidService financialAidService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public AdminDashboardController(
            ScholarshipService scholarshipService,
            FinancialAidService financialAidService,
            ApplicationService applicationService,
            UserService userService
    ) {
        this.scholarshipService = scholarshipService;
        this.financialAidService = financialAidService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboard(
            @RequestBody Map<String, Long> requestBody
    ) {
        Long userId = requestBody.get("id");
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "User ID is required", null));
        }

        // Fetch admin/user profile via service
        ApiResponse<UserResponse> userResponse = userService.getById(userId);
        if (!userResponse.isSuccess() || userResponse.getData() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Admin not found", null));
        }

        UserResponse adminProfile = userResponse.getData();

        // Fetch scholarships, financial aids, applications
        List<Scholarship> scholarships = scholarshipService.getAll();
        List<FinancialAid> financialAid = financialAidService.getAll();
        List<Application> applications = applicationService.getAll();

        // Build the response map
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("adminProfile", adminProfile);
        responseData.put("scholarships", scholarships);
        responseData.put("financialAid", financialAid);
        responseData.put("applications", applications);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Admin dashboard data fetched", responseData)
        );
    }
}