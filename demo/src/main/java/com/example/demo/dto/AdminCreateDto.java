package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateDto {
    private String email;
    private String club;
    private String password; // plaintext från frontend
}
