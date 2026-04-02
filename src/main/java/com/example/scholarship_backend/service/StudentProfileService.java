package com.example.scholarship_backend.service;

import com.example.scholarship_backend.model.StudentProfile;
import com.example.scholarship_backend.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import com.example.scholarship_backend.repository.UserRepository;

@Service
public class StudentProfileService {

    private final StudentProfileRepository repo;
    private final UserRepository userRepository;

    public StudentProfileService(StudentProfileRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public StudentProfile getByUserId(Long userId) {
        return repo.findByUserId(userId).orElseGet(() -> {
            StudentProfile profile = new StudentProfile();

            // Optional: attach user (recommended)
            profile.setUser(userRepository.findById(userId).orElseThrow());

            return profile;
        });
    }

    public StudentProfile save(StudentProfile profile) {
        return repo.save(profile);
    }

    public StudentProfile updateProfile(Long userId, StudentProfile updatedData) {

        StudentProfile existing = repo.findByUserId(userId)
                .orElseGet(() -> {
                    StudentProfile p = new StudentProfile();
                    p.setUser(userRepository.findById(userId).orElseThrow());
                    return p;
                });

        // ✅ Only update NON-NULL fields

        if (updatedData.getFullName() != null) {
            existing.setFullName(updatedData.getFullName());
        }

        if (updatedData.getPhone() != null) {
            existing.setPhone(updatedData.getPhone());
        }

        if (updatedData.getMajor() != null) {
            existing.setMajor(updatedData.getMajor());
        }

        if (updatedData.getGpa() != null) {
            existing.setGpa(updatedData.getGpa());
        }

        if (updatedData.getUniversity() != null) {
            existing.setUniversity(updatedData.getUniversity());
        }

        if (updatedData.getEnrollmentDate() != null) {
            existing.setEnrollmentDate(updatedData.getEnrollmentDate());
        }

        return repo.save(existing);
    }

}