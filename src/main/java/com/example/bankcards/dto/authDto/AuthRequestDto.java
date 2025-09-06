package com.example.bankcards.dto.authDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDto {
    @Email
    private String email;
    @NotBlank
    private String password;
}
