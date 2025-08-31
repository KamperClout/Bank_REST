package com.example.bankcards.service.auth;

import com.example.bankcards.dto.authDto.AuthRequestDto;
import com.example.bankcards.dto.authDto.AuthResponseDto;
import com.example.bankcards.dto.authDto.RegisterRequestDto;


public interface AuthService {
    AuthResponseDto register(RegisterRequestDto registerRequestDto);
    AuthResponseDto login(AuthRequestDto authRequestDto);
    AuthResponseDto refreshToken(String refreshToken);
}
