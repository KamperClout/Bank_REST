package com.example.bankcards.service.user;

import com.example.bankcards.dto.authDto.RegisterRequestDto;
import com.example.bankcards.dto.userDto.UpdateUserRequest;
import com.example.bankcards.dto.userDto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(RegisterRequestDto registerRequestDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUser(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);

}
