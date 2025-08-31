package com.example.bankcards.service.user;

import com.example.bankcards.dto.authDto.RegisterRequestDto;
import com.example.bankcards.dto.userDto.UpdateUserRequest;
import com.example.bankcards.dto.userDto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.userException.UserExistsException;
import com.example.bankcards.exception.userException.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.auth.AuthService;
import com.example.bankcards.entity.Role;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UserExistsException("User already exists with email "
                    + registerRequestDto.getEmail());
        }
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);
        user.setName(registerRequestDto.getName());
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
       User user = userRepository.findById(id).orElseThrow(()
               -> new UserNotFoundException("User not found with id " + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException("User not found with email " + email));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("User not found with id " + id));
        if(updateUserRequest.getName()!=null) {
            user.setName(updateUserRequest.getName());
        }
        if(updateUserRequest.getEmail()!=null && !updateUserRequest.getEmail().equals(user.getEmail())) {
            if(userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new UserExistsException("Email already exists");
            }
            user.setEmail(updateUserRequest.getEmail());
        }
        user.setRole(updateUserRequest.getRole());
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id " + id));
        userRepository.deleteById(id);
    }
}
