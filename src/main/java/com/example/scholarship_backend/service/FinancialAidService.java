package com.example.scholarship_backend.service;

import com.example.scholarship_backend.model.FinancialAid;
import com.example.scholarship_backend.repository.FinancialAidRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.scholarship_backend.repository.ApplicationRepository;
import com.example.scholarship_backend.model.Application.Status;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FinancialAidService {

    private final FinancialAidRepository repo;
    private final ApplicationRepository applicationRepo;

    public FinancialAidService(FinancialAidRepository repo, ApplicationRepository applicationRepo) {
        this.repo = repo;
        this.applicationRepo = applicationRepo;
    }

    public FinancialAid create(FinancialAid aid) {
        return repo.save(aid);
    }

    public List<FinancialAid> getAll() {
        return repo.findAll();
    }

    public FinancialAid update(Long id, FinancialAid updated) {
        FinancialAid aid = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial Aid not found"));

        if (updated.getTitle() != null)
            aid.setTitle(updated.getTitle());
        if (updated.getType() != null)
            aid.setType(updated.getType());
        if (updated.getAmount() != null)
            aid.setAmount(updated.getAmount());
        if (updated.getInterestRate() != null)
            aid.setInterestRate(updated.getInterestRate());
        if (updated.getDeadline() != null)
            aid.setDeadline(updated.getDeadline());
        if (updated.getDescription() != null)
            aid.setDescription(updated.getDescription());
        if (updated.getRequirements() != null)
            aid.setRequirements(updated.getRequirements());

        return repo.save(aid);
    }

    public void delete(Long id) {
        FinancialAid financialAid = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial Aid not found"));

        // Check if there are any approved applications for this financial aid
        boolean hasApprovedApplications = applicationRepo.existsByFinancialAidAndStatus(financialAid, Status.APPROVED);

        if (hasApprovedApplications) {
            throw new RuntimeException(
                    "Cannot delete this financial aid because it has already been approved for students. Approved financial aid cannot be deleted to maintain data integrity.");
        }

        repo.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public Optional<FinancialAid> findById(Long id) {
        return repo.findById(id);
    }

    public List<FinancialAid> getByAdminId(Long adminId) {
        return repo.findByAdminId(adminId);
    }
}