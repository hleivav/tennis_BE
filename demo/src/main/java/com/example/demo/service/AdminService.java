package com.example.demo.service;

import com.example.demo.dto.AdminCreateDto;
import com.example.demo.dto.AdminDto;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;   // <-- lägg till denna import

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }



    public List<AdminDto> getAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AdminDto create(AdminCreateDto dto) {
        Admin admin = new Admin();
        admin.setEmail(dto.getEmail());
        admin.setClub(dto.getClub());
        admin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return toDto(repo.save(admin));
    }

    public AdminDto findByEmail(String email) {
        Optional<Admin> adminOpt = repo.findByEmail(email);
        if (adminOpt.isPresent()) {
            return toDto(adminOpt.get());
        }
        return null;
    }

    private AdminDto toDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setEmail(admin.getEmail());
        dto.setClub(admin.getClub());
        return dto;
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}
