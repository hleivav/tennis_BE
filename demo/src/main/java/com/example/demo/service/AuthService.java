package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(password, admin.getPasswordHash())) {
                return new AuthResponse("dummy-token", admin.getId());
            }
        }
        return null;
    }
}