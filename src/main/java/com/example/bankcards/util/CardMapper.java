package com.example.bankcards.util;

import com.example.bankcards.dto.cardDto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public static Card toCard(CardDto cardDto, User cardOwner) {
        return Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .cardHolderName(cardOwner)
                .cardStatus(cardDto.getCardStatus())
                .balance(cardDto.getBalance())
                .build();
    }

    public static CardDto toCardDto(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardHolderName(card.getCardHolderName().getName())
                .expiryDate(card.getExpiryDate())
                .balance(card.getBalance())
                .cardStatus(card.getCardStatus())
                .build();
    }

    public static String maskCard(String cardNumber) {
        return "**** **** **** " + cardNumber;
    }
}
