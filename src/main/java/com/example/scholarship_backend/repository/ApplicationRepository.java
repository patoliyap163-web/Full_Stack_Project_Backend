package com.example.scholarship_backend.repository;

import com.example.scholarship_backend.model.Application;
import com.example.scholarship_backend.model.User;
import com.example.scholarship_backend.model.Scholarship;
import com.example.scholarship_backend.model.FinancialAid;
import com.example.scholarship_backend.model.Application.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // ✅ Get all applications of a student
    List<Application> findByStudent(User student);

    // ✅ Check if student already has an approved application
    boolean existsByStudentAndStatus(User student, Status status);

    // ✅ Delete all other applications except the approved one
    void deleteByStudentAndIdNot(User student, Long id);

    List<Application> findByScholarshipAdminId(Long adminId);

    List<Application> findByFinancialAidAdminId(Long adminId);

    // ✅ Check if scholarship has any approved applications
    boolean existsByScholarshipAndStatus(Scholarship scholarship, Status status);

    // ✅ Check if financial aid has any approved applications
    boolean existsByFinancialAidAndStatus(FinancialAid financialAid, Status status);
}