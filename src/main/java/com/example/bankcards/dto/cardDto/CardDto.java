package com.example.bankcards.dto.cardDto;


import com.example.bankcards.entity.CardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CardDto {
    private UUID id;
    @NotBlank(message = "номер карты не может быть пустым")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать ровно 16 цифр")
    private String cardNumber;
    @NotBlank(message = "ФИО не могут быть пустыми")
    private String cardHolderName;
    @NotBlank(message = "Срок действия карты обязателен")
    @Pattern(
            regexp = "^(0[1-9]|1[0-2])/\\d{2}$",
            message = "Срок действия должен быть в формате MM/yy и с правильным месяцем"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String expiryDate;
    @NotNull
    private CardStatus cardStatus;
    @PositiveOrZero(message = "баланс может быть только положительным или нулевым")
    private BigDecimal balance;

}
