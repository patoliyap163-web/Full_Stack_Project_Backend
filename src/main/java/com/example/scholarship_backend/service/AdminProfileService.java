package com.example.scholarship_backend.service;

import com.example.scholarship_backend.model.AdminProfile;
import com.example.scholarship_backend.repository.AdminProfileRepository;
import org.springframework.stereotype.Service;
import com.example.scholarship_backend.repository.UserRepository;


@Service
public class AdminProfileService {

    private final AdminProfileRepository repo;
    private final UserRepository userRepository;

    public AdminProfileService(AdminProfileRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public AdminProfile getByUserId(Long userId) {
    return repo.findByUserId(userId).orElseGet(() -> {
        AdminProfile profile = new AdminProfile();

        // optional but recommended
        profile.setUser(userRepository.findById(userId).orElseThrow());

        return profile;
    });
}

    public AdminProfile updateProfile(Long userId, AdminProfile updatedData) {

        AdminProfile existing = repo.findByUserId(userId)
                .orElseGet(() -> {
                    AdminProfile p = new AdminProfile();
                    p.setUser(userRepository.findById(userId).orElseThrow());
                    return p;
                });

        if (updatedData.getPhone() != null) {
            existing.setPhone(updatedData.getPhone());
        }

        if (updatedData.getDepartment() != null) {
            existing.setDepartment(updatedData.getDepartment());
        }

        if (updatedData.getRoleName() != null) {
            existing.setRoleName(updatedData.getRoleName());
        }

        if (updatedData.getLastLogin() != null) {
            existing.setLastLogin(updatedData.getLastLogin());
        }

        return repo.save(existing);
    }

    public AdminProfile save(AdminProfile profile) {
        return repo.save(profile);
    }
}