package com.homeBanking.cardsservice.service;


import com.homeBanking.cardsservice.entities.Card;
import com.homeBanking.cardsservice.exceptions.BadRequestException;
import com.homeBanking.cardsservice.exceptions.ConflictException;
import com.homeBanking.cardsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.cardsservice.repository.CardRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Transactional
    public Card registerCard(Card card) throws ConflictException, BadRequestException {
        checkCardData(card);
        Optional<Card> cardOptional = cardRepository.findByNumber(card.getNumber());
        if(cardOptional.isPresent()) {
            throw new ConflictException("Card already exists");
        } else {
            return cardRepository.save(card);
        }
    }

    private void checkCardData(Card card) throws BadRequestException {
        if(card.getAccountId() == null) {
            throw new BadRequestException("No account associated");
        }
        if(card.getHolder()==null || card.getHolder().equals("")) {
            throw new BadRequestException("Holder missing");
        }
        if(card.getNumber() == null || card.getNumber().length()!=16) {
            throw new BadRequestException("Number missing");
        }
        if(card.getExpirationDate() == null) {
            throw new BadRequestException("Expiration date missing");
        }
        if(card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Card already expired");
        }
        if(card.getCvv() == null || card.getCvv().equals("")) {
            throw new BadRequestException("CVV missing");
        }
    }

    public Card getCardByIdAndAccountId(Long cardId, Long accountId) throws ResourceNotFoundException {
        Optional<Card> cardOptional = cardRepository.findByIdAndAccountId(cardId, accountId);

        if(cardOptional.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            return cardOptional.get();
        }
    }

    public Card getCardByNumberAndAccountId(String cardNumber, Long accountId) throws ResourceNotFoundException {
        Optional<Card> cardOptional = cardRepository.findByNumberAndAccountId(cardNumber, accountId);
        if(cardOptional.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            return cardOptional.get();
        }
    }

    public List<Card> getAllCardsByAccountId(Long accountId) throws ResourceNotFoundException {
        Optional<List<Card>> optionalCardList = cardRepository.findAllByAccountId(accountId);
        if (optionalCardList.isEmpty()) {
            throw new ResourceNotFoundException("No card list found");
        }
        return optionalCardList.get();
    }
    @Transactional
    public void deleteCard(Long cardId, Long accountId) throws ResourceNotFoundException {
        Optional<Card> optionalCard = cardRepository.findByIdAndAccountId(cardId, accountId);
        if(optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            Card cardFound = optionalCard.get();
            cardRepository.deleteByIdAndAccountId(cardFound.getId(), accountId);
        }
    }
}
