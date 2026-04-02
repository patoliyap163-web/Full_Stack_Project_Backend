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
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dashboard")
public class StudentDashboardController {

        private final UserService userService;
        private final ScholarshipService scholarshipService;
        private final FinancialAidService financialAidService;
        private final ApplicationService applicationService;

        public StudentDashboardController(UserService userService,
                        ScholarshipService scholarshipService,
                        FinancialAidService financialAidService,
                        ApplicationService applicationService) {
                this.userService = userService;
                this.scholarshipService = scholarshipService;
                this.financialAidService = financialAidService;
                this.applicationService = applicationService;
        }

        @PostMapping("/student")
        public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentDashboard(
                        @RequestBody Map<String, Long> requestBody) {
                Long userId = requestBody.get("id");
                if (userId == null) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse<>(false, "User ID is required", null));
                }

                // Fetch student profile
                var studentResponse = userService.getById(userId); // returns ApiResponse<UserResponse>
                if (!studentResponse.isSuccess() || studentResponse.getData() == null) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse<>(false, "Student not found", null));
                }
                UserResponse studentProfile = studentResponse.getData();

                // Fetch scholarships, financial aids
                List<Scholarship> scholarships = scholarshipService.getAll();
                List<FinancialAid> financialAid = financialAidService.getAll();

                // Fetch applications for this student
                List<Application> applications = applicationService.getAll()
                                .stream()
                                .filter(app -> app.getStudent() != null && app.getStudent().getId().equals(userId))
                                .collect(Collectors.toList());

                // Build response map
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("studentProfile", studentProfile);
                responseData.put("scholarships", scholarships);
                responseData.put("financialAid", financialAid);
                responseData.put("applications", applications);

                return ResponseEntity.ok(
                                new ApiResponse<>(true, "Student dashboard data fetched", responseData));
        }
}