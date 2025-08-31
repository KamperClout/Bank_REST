package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "number", unique = true, nullable = false)
    @JsonProperty("card_number")
    String cardNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonProperty("card_holder")
    User cardHolderName;
    @Column(name = "expiryDate", nullable = false)
    @JsonProperty(value = "expiry_date", access = JsonProperty.Access.READ_ONLY)
    String expiryDate = LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("MM/yy"));
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    CardStatus cardStatus;
    @Column(name = "balance", nullable = false)
    @JsonProperty("balance")
    BigDecimal balance;

}
