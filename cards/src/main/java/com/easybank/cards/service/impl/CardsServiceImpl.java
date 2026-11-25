package com.easybank.cards.service.impl;

import com.easybank.cards.dto.CardsDto;
import com.easybank.cards.constants.CardsConstants;
import com.easybank.cards.entity.Cards;
import com.easybank.cards.exception.CardAlreadyExistsException;
import com.easybank.cards.exception.ResourceNotFoundException;
import com.easybank.cards.mapper.CardsMapper;
import com.easybank.cards.repository.CardsRepository;
import com.easybank.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;;


    @Override
    public void createCard(String mobileNumber) {
        // Implementation for creating a card
        Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
        if (optionalCards.isPresent()) {
            log.warn("Card already registered with mobileNumber: {}", mobileNumber);
            throw new CardAlreadyExistsException("Card already exists for mobile number: " + mobileNumber);
        }
        // Create a new card
        Cards savedCard = cardsRepository.save(createNewCard(mobileNumber));
        log.debug("Created cardNumber: {} for mobileNumber: {}", savedCard.getCardNumber(), mobileNumber);
    }

    private Cards createNewCard(String mobileNumber) {
        // Implementation for creating a new card
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    @Override
    public CardsDto getCard(String mobileNumber) {
        // Implementation for retrieving a card
        log.debug("Looking up card for mobileNumber: {}", mobileNumber);
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        // Implementation for updating a card
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "cardNumber", cardsDto.getCardNumber())
        );

        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        log.debug("Updated cardNumber: {}", cards.getCardNumber());
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        // Implementation for deleting a card
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.delete(cards);
        log.debug("Deleted cardNumber: {} for mobileNumber: {}", cards.getCardNumber(), mobileNumber);
        return true;
    }
}
