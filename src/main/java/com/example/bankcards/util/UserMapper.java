package com.example.bankcards.util;

import com.example.bankcards.dto.userDto.UserDto;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static User toUser(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }
    public static UserDto toUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
