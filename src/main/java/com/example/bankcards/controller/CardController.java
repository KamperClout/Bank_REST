package com.example.bankcards.controller;

import com.example.bankcards.dto.cardDto.CardDto;
import com.example.bankcards.dto.cardDto.TransferRequestDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
@PreAuthorize("hasRole('USER')")
public class CardController {
    private final CardService cardService;
    private final UserService userService;

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDto transferRequestDto,
                                         @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        cardService.transfer(userId, transferRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get_all_userCards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<CardDto>> getAllUserCards(@AuthenticationPrincipal User user,
                                                         @RequestParam(required = false) String search,
                                                         @PageableDefault(
                                                                 sort = "expiryDate", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {
        Long userId = user.getId();
        return ResponseEntity.ok(cardService.getAllUserCards(userId, search, pageable));

    }

    @GetMapping("/get_balance/{cardNumber}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String cardNumber,
                                                 @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        return ResponseEntity.ok(cardService.getBalance(cardNumber, userId));
    }
}
