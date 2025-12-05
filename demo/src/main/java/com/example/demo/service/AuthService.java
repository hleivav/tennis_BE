package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPasswordHash())) {
            // Return a dummy token and adminId
            return new AuthResponse("dummy-token", admin.getId());
        }
        return null;
    }
}