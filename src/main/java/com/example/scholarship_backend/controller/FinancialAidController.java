package com.example.scholarship_backend.controller;

import com.example.scholarship_backend.model.FinancialAid;
import com.example.scholarship_backend.dto.ApiResponse;
import com.example.scholarship_backend.service.FinancialAidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://fullstackproject-nu.vercel.app")
@RestController
@RequestMapping("/api/financial-aid")
public class FinancialAidController {

    private final FinancialAidService service;

    public FinancialAidController(FinancialAidService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody FinancialAid aid) {
        FinancialAid created = service.create(aid);
        return ResponseEntity.ok(new ApiResponse(true, "Financial Aid created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<FinancialAid> aids = service.getAll();
        return ResponseEntity.ok(new ApiResponse(true, "All financial aid fetched", aids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(aid -> ResponseEntity.ok(new ApiResponse(true, "Financial Aid found", aid)))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse(false, "Financial Aid not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody FinancialAid updated) {
        if (!service.existsById(id)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Financial Aid not found"));
        }
        FinancialAid aid = service.update(id, updated);
        return ResponseEntity.ok(new ApiResponse(true, "Financial Aid updated", aid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Financial Aid not found"));
        }
        try {
            service.delete(id);
            return ResponseEntity.ok(new ApiResponse(true, "Financial Aid deleted"));
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Cannot delete this financial aid")) {
                message = "This financial aid cannot be deleted because it has already been approved for one or more students. Deleting approved financial aid would compromise data integrity and student records.";
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, message));
        }
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<List<FinancialAid>>> getByAdmin(@PathVariable Long adminId) {
        List<FinancialAid> aids = service.getByAdminId(adminId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Financial aids fetched successfully", aids));
    }
}