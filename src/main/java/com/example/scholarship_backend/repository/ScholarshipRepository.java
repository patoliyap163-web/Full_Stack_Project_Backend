package com.example.scholarship_backend.repository;

import com.example.scholarship_backend.model.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.scholarship_backend.model.User;
import java.util.List;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    List<Scholarship> findByAdmin(User admin);
}