package com.example.bankcards.service.card;

import com.example.bankcards.dto.cardDto.CardDto;
import com.example.bankcards.dto.cardDto.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    CardDto create(Long userId);

    List<CardDto> getAll();

    void deleteCard(String cardNumber, Long userId);

    BigDecimal getBalance(String cardNumber, Long userId);

    CardDto activateCard(String cardNumber, Long userId);

    void transfer(Long userId, TransferRequestDto transferRequestDto);

    Page<CardDto> getAllUserCards(Long userId, String search, Pageable pageable);

    CardDto blockCard(Long userId, String cardNumber);

}
