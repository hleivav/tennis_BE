package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
//en liten kommentar
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://tennis-app-one.vercel.app"})
public class PasswordResetController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        if (!adminOpt.isPresent()) {
            // Säkerhet: säg inte om e-posten finns eller inte
            return ResponseEntity.ok("Om e-posten finns får du ett återställningsmail");
        }

        Admin admin = adminOpt.get();

        // Generera token
        String token = UUID.randomUUID().toString();
        admin.setResetToken(token);
        admin.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        adminRepository.save(admin);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Optional<Admin> adminOpt = adminRepository.findByResetToken(token);

        if (!adminOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Ogiltig eller utgången token");
        }

        Admin admin = adminOpt.get();

        if (admin.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token har utgått");
        }

        // Uppdatera lösenord - VIKTIGT: hasha först!
        admin.setPasswordHash(passwordEncoder.encode(newPassword));
        admin.setResetToken(null);
        admin.setResetTokenExpiry(null);
        adminRepository.save(admin);

        return ResponseEntity.ok("Lösenord uppdaterat");
    }
}