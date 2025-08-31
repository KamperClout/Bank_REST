package com.example.bankcards.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class CardNumberGenerator {
    private static final String BIN = "1710";

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(BIN);
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        String cardNumber = sb.toString();
        return maskCardNumber(cardNumber);
    }

    static String maskCardNumber(String cardNumber) {
        if (cardNumber.length() == 16) {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return null;
    }
}
