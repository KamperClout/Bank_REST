package com.example.bankcards.dto.userDto;

import com.example.bankcards.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    @Positive(message = "Id может быть только положительным")
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private Role role;
}
