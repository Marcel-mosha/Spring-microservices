package com.easybank.cards.service;

import com.easybank.cards.dto.CardsDto;


public interface ICardsService {

    void createCard(String mobileNumber);

    CardsDto getCard(String mobileNumber);

    boolean updateCard(CardsDto cardsDto);

    boolean deleteCard(String mobileNumber);
}
