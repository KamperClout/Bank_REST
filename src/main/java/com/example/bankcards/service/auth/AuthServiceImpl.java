package com.example.bankcards.service.auth;

import com.example.bankcards.dto.authDto.AuthRequestDto;
import com.example.bankcards.dto.authDto.AuthResponseDto;
import com.example.bankcards.dto.authDto.RegisterRequestDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.entity.Role;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UsernameNotFoundException("Email Already Exists");
        }
        User user = User.builder()
                .name(registerRequestDto.getName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponseDto(token, refreshToken, UserMapper.toUserDto(user));
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponseDto(token, refreshToken, UserMapper.toUserDto(user));
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        String userName = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userName).orElseThrow(
                () -> new UsernameNotFoundException("Username Not Found"));
        if (jwtService.isTokenValid(refreshToken, user)) {
            String newAccessToken = jwtService.generateToken(user);
            return new AuthResponseDto(newAccessToken, refreshToken, UserMapper.toUserDto(user));
        } else throw new RuntimeException("Invalid Refresh Token or expired");
    }
}
