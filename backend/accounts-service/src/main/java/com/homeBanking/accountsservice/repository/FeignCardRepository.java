package com.homeBanking.accountsservice.repository;


import com.homeBanking.accountsservice.entities.Card;
import com.homeBanking.accountsservice.entities.CardResquestWithAccountId;
import com.homeBanking.accountsservice.feignCustomExceptions.CustomErrorDecoder;
import com.homeBanking.accountsservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "cards-service",
        path = "/card",
        configuration = {FeignConfig.class, CustomErrorDecoder.class}
)
public interface FeignCardRepository {

    @PostMapping("/register-card")
    Card registerCard(@RequestBody CardResquestWithAccountId card);

    @GetMapping("/{id}/all-cards")
    List<Card> getAllCardsByAccountId(@PathVariable Long id);

    @GetMapping("/{accountId}/card/{cardId}")
    Card getCardByIdAndAccountId (@PathVariable Long accountId, @PathVariable Long cardId);

    @DeleteMapping("/{accountId}/card/{cardId}")
    void deleteCard(@PathVariable Long accountId, @PathVariable Long cardId);

    @GetMapping("/{accountId}/cardNumber/{cardNumber}")
    Card getCardByNumberAndAccountId (@PathVariable Long accountId, @PathVariable String cardNumber);

}
