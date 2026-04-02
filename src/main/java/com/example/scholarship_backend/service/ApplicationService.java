package com.example.scholarship_backend.service;

import com.example.scholarship_backend.model.Application;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository repo;

    public ApplicationService(ApplicationRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE APPLICATION
    public Application create(Application app) {

        User student = app.getStudent();

        // ❌ Prevent applying if already approved somewhere
        boolean alreadyApproved = repo.existsByStudentAndStatus(student, Application.Status.APPROVED);

        if (alreadyApproved) {
            throw new RuntimeException("You already have an approved application");
        }

        // ❌ Ensure only one of scholarship or financial aid is selected
        if (app.getScholarship() == null && app.getFinancialAid() == null) {
            throw new RuntimeException("Application must have either scholarship or financial aid");
        }

        return repo.save(app);
    }

    // ✅ GET ALL
    public List<Application> getAll() {
        return repo.findAll();
    }

    // ✅ GET BY STUDENT (UPDATED)
    public List<Application> getByStudent(User student) {
        return repo.findByStudent(student);
    }

    // ✅ APPROVE / REJECT
    public Application updateStatus(Long id, Application.Status status) {

        Application app = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        User student = app.getStudent();

        // 🔥 If approving → apply your main rule
        if (status == Application.Status.APPROVED) {

            boolean alreadyApproved = repo.existsByStudentAndStatus(student, Application.Status.APPROVED);

            if (alreadyApproved) {
                throw new RuntimeException("Student already has an approved application");
            }

            app.setStatus(Application.Status.APPROVED);

            // 🔥 Delete all other applications of this student
            repo.deleteByStudentAndIdNot(student, app.getId());

        } else {
            app.setStatus(status);
        }

        return repo.save(app);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ EXISTS
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public Map<String, List<Application>> getApplicationsByAdmin(Long adminId) {

        List<Application> scholarshipApps = repo.findByScholarshipAdminId(adminId);

        List<Application> financialAidApps = repo.findByFinancialAidAdminId(adminId);

        Map<String, List<Application>> result = new HashMap<>();
        result.put("scholarshipApplications", scholarshipApps);
        result.put("financialAidApplications", financialAidApps);

        return result;
    }
}