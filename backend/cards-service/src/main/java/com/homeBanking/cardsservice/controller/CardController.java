package com.homeBanking.cardsservice.controller;


import com.homeBanking.cardsservice.entities.Card;
import com.homeBanking.cardsservice.exceptions.BadRequestException;
import com.homeBanking.cardsservice.exceptions.ConflictException;
import com.homeBanking.cardsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.cardsservice.service.CardService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/{id}/all-cards")
    public ResponseEntity<?> getAllCardsByAccountId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardsByAccountId(id));
    }

    @GetMapping("/{accountId}/card/{cardId}")
    public ResponseEntity<?> getCardByIdAndAccountId (@PathVariable Long accountId, @PathVariable Long cardId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardByIdAndAccountId(cardId, accountId));
    }

    @GetMapping("/{accountId}/cardNumber/{cardNumber}")
    public ResponseEntity<?> getCardByNumberAndAccountId (@PathVariable Long accountId, @PathVariable String cardNumber) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardByNumberAndAccountId(cardNumber, accountId));
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerCard(@RequestBody Card card) throws BadRequestException, ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.registerCard(card));
    }

    @DeleteMapping("/{accountId}/card/{cardId}")
    @Transactional
    public ResponseEntity<?> deleteCard(@PathVariable Long accountId, @PathVariable Long cardId) throws ResourceNotFoundException {
        cardService.deleteCard(cardId, accountId);
        return ResponseEntity.ok().build();
    }
}
