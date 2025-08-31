package com.example.bankcards.dto.cardDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать ровно 16 цифр")
    String fromCardNumber;
    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать ровно 16 цифр")
    String toCardNumber;
    @Positive
    @NotBlank
    BigDecimal amount;
}
