package com.example.bankcards.service.card;

import com.example.bankcards.dto.cardDto.CardDto;
import com.example.bankcards.dto.cardDto.TransferRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.cardException.CardNotFoundException;
import com.example.bankcards.exception.cardException.CardTransferException;
import com.example.bankcards.exception.cardException.StatusConflictException;
import com.example.bankcards.exception.userException.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.entity.CardStatus;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final RestClient.Builder builder;

    @Override
    public CardDto create(Long userId) {
        User cardHolder = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        Card card = new Card();
        card.setCardNumber(CardNumberGenerator.generateCardNumber());
        card.setCardHolderName(cardHolder);
        card.setBalance(new BigDecimal(0));
        card.setCardStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
        return CardMapper.toCardDto(card);
    }

    @Override
    public List<CardDto> getAll() {
        return cardRepository.findAll().stream().map(CardMapper::toCardDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCard(String cardNumber, Long userId) {
        User cardHolder = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        Card card = cardRepository.findCardByCardNumberAndCardHolderName(CardMapper.maskCard(cardNumber),
                cardHolder).orElseThrow(() ->
                new CardNotFoundException("Card not found"));
        cardRepository.deleteById(card.getId());
    }

    @Override
    public BigDecimal getBalance(String cardNumber, Long userId) {
        User cardHolder = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        Card card = cardRepository.findCardByCardNumber(CardMapper.maskCard(cardNumber)).orElseThrow(() ->
                new CardNotFoundException("Card not found"));
        return card.getBalance();
    }

    @Override
    @Transactional
    public CardDto activateCard(String cardNumber, Long userId) {
        User cardHolder = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("User not found with id: " + userId));
        Card card = cardRepository.findCardByCardNumberAndCardHolderName(CardMapper.maskCard(cardNumber),
                cardHolder).orElseThrow(() ->
                new CardNotFoundException("Card not found"));
        if(card.getCardStatus().equals(CardStatus.ACTIVE)){
            throw new StatusConflictException("status conflict");
        }
        card.setCardStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
        return CardMapper.toCardDto(card);
    }

    @Override
    @Transactional
    public void transfer(Long userId,TransferRequestDto transferRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("User not found with id: " + userId));
        if (transferRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than 0");
        }
        Card sendCard = cardRepository.findCardByCardNumberAndCardHolderName(
                transferRequestDto.getFromCardNumber(),user).orElseThrow(
                () -> new CardNotFoundException("Card not found")
        );
        if(sendCard.getBalance().compareTo(transferRequestDto.getAmount()) <= 0){
            throw new CardTransferException("SendCard balance must be greater than 0");
        }
        Card reciveCard = cardRepository.findCardByCardNumberAndCardHolderName(
                transferRequestDto.getToCardNumber(),user).orElseThrow(
                () -> new CardNotFoundException("Card not found"));
        if(!sendCard.getCardStatus().equals(CardStatus.ACTIVE) ||
                !reciveCard.getCardStatus().equals(CardStatus.ACTIVE)){
            throw new CardTransferException("One of card blocked can't be transferred");
        }
        sendCard.setBalance(sendCard.getBalance().subtract(transferRequestDto.getAmount()));
        reciveCard.setBalance(reciveCard.getBalance().add(transferRequestDto.getAmount()));
        cardRepository.save(sendCard);
        cardRepository.save(reciveCard);
    }

    @Override
    public Page<CardDto> getAllUserCards(Long userId, String search, Pageable pageable) {
        Specification<Card> specification = searchCard(userId, search);
        return cardRepository.findAll(specification, pageable)
                .map(CardMapper::toCardDto);
    }

    @Override
    @Transactional
    public CardDto blockCard(Long userId, String cardNumber) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found"));
        Card card = cardRepository.findCardByCardNumberAndCardHolderName(CardMapper.maskCard(cardNumber),
                user).orElseThrow(
                ()-> new CardNotFoundException("Card not found"));
        card.setCardStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
        return CardMapper.toCardDto(card);
    }

    private Specification<Card> searchCard(Long id, String search) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("cardHolderName").get("id"), id));
            if (search != null && !search.isBlank()) {
                Predicate predicateNumber = criteriaBuilder.like(criteriaBuilder.lower(root.get("cardNumber")),
                        "%" + search.toLowerCase() + "%");
                Predicate predicateStatus = criteriaBuilder.like(criteriaBuilder.lower(root.get("cardStatus")),"%"
                        + search.toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(predicateNumber, predicateStatus));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
