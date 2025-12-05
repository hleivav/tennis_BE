package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


    public class AuthResponse {
        private String token;
        private Long adminId;

        public AuthResponse(String token, Long adminId) {
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