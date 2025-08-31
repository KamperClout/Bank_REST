package com.example.bankcards.dto.authDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 20)
    private String password;
}
