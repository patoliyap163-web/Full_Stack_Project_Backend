package com.example.scholarship_backend.service;

import com.example.scholarship_backend.model.Scholarship;
import com.example.scholarship_backend.repository.ScholarshipRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.repository.ApplicationRepository;
import com.example.scholarship_backend.model.Application.Status;

import java.util.List;

@Service
@Transactional
public class ScholarshipService {

    private final ScholarshipRepository repo;
    private final ApplicationRepository applicationRepo;

    public List<Scholarship> getByAdmin(User admin) {
        return repo.findByAdmin(admin);
    }

    public ScholarshipService(ScholarshipRepository repo, ApplicationRepository applicationRepo) {
        this.repo = repo;
        this.applicationRepo = applicationRepo;
    }

    public Scholarship create(Scholarship s) {
        return repo.save(s);
    }

    public List<Scholarship> getAll() {
        return repo.findAll();
    }

    public Scholarship update(Long id, Scholarship updated) {
        Scholarship s = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        if (updated.getTitle() != null)
            s.setTitle(updated.getTitle());
        if (updated.getCategory() != null)
            s.setCategory(updated.getCategory());
        if (updated.getAmount() != null)
            s.setAmount(updated.getAmount());
        if (updated.getGpa() != null)
            s.setGpa(updated.getGpa());
        if (updated.getDeadline() != null)
            s.setDeadline(updated.getDeadline());
        if (updated.getDescription() != null)
            s.setDescription(updated.getDescription());
        if (updated.getEligibility() != null)
            s.setEligibility(updated.getEligibility());
        if (updated.getBenefits() != null)
            s.setBenefits(updated.getBenefits());
        if (updated.getRequirements() != null)
            s.setRequirements(updated.getRequirements());

        return repo.save(s);
    }

    public void delete(Long id) {
        Scholarship scholarship = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        // Check if there are any approved applications for this scholarship
        boolean hasApprovedApplications = applicationRepo.existsByScholarshipAndStatus(scholarship, Status.APPROVED);

        if (hasApprovedApplications) {
            throw new RuntimeException(
                    "Cannot delete this scholarship because it has already been approved for students. Approved scholarships cannot be deleted to maintain data integrity.");
        }

        repo.deleteById(id);
    }

    // ✅ Add this method to fix the controller error
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }
}