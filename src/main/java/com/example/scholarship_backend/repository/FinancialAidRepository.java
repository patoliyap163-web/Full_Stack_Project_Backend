package com.example.scholarship_backend.repository;

import com.example.scholarship_backend.model.FinancialAid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialAidRepository extends JpaRepository<FinancialAid, Long> {
    List<FinancialAid> findByAdminId(Long adminId);
}