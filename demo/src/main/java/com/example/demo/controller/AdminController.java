package com.example.demo.controller;

import com.example.demo.dto.AdminCreateDto;
import com.example.demo.dto.AdminDto;
import com.example.demo.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdminDto> getAllAdmins() {
        return service.getAll();
    }

    @PostMapping
    public AdminDto createAdmin(@RequestBody AdminCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{email}")
    public AdminDto getByEmail(@PathVariable String email) {
        return service.findByEmail(email);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        service.deleteById(id);
    }

}
