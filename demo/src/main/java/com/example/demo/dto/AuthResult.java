package com.example.demo.dto;

public class AuthResult {
    private String token;
    private Long adminId;

    public AuthResult(String token, Long adminId) {
        this.token = token;
        this.adminId = adminId;
    }

    public String getToken() {
        return token;
    }

    public Long getAdminId() {
        return adminId;
    }
}