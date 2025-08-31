package com.example.bankcards.dto.authDto;

import com.example.bankcards.dto.userDto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    @JsonProperty("user")
    private UserDto userDto;
}
