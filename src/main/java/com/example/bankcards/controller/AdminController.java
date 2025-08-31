package com.example.bankcards.controller;

import com.example.bankcards.dto.authDto.RegisterRequestDto;
import com.example.bankcards.dto.cardDto.CardDto;
import com.example.bankcards.dto.userDto.UpdateUserRequest;
import com.example.bankcards.dto.userDto.UserDto;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final CardService cardService;
    private final UserService userService;

    @PostMapping("/users/createUser")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(userService.createUser(registerRequestDto));
    }

    @GetMapping("/users/get_by_id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(userId, updateUserRequest));
    }

    @GetMapping("/users/getAll")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/get_by_email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/cards/create/{userId}")
    public ResponseEntity<CardDto> createCard(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.create(userId));
    }

    @DeleteMapping("/cards/delete/{cardNumber}/users/{userId}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber,
                                           @PathVariable Long userId) {
        cardService.deleteCard(cardNumber, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cards/activate/{cardNumber}/users/{userId}")
    public ResponseEntity<CardDto> activateCard(@PathVariable String cardNumber, @PathVariable Long userId) {
        return ResponseEntity.ok(cardService.activateCard(cardNumber, userId));
    }

    @PostMapping("/cards/block/{cardNumber}/users/{userId}")
    public ResponseEntity<CardDto> blockCard(@PathVariable String cardNumber, @PathVariable Long userId) {
        return ResponseEntity.ok(cardService.blockCard(userId,cardNumber));
    }
}
