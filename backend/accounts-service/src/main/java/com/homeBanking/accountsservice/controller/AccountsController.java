package com.homeBanking.accountsservice.controller;


import com.homeBanking.accountsservice.entities.*;
import com.homeBanking.accountsservice.exceptions.CheckTransactionDataException;
import com.homeBanking.accountsservice.exceptions.InsufficientFundsException;
import com.homeBanking.accountsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.accountsservice.service.AccountsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
    @Autowired
    private AccountsService accountsService;

    @PostMapping("/create")
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        accountsService.createAccount(accountRequest.getUserId());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAccount() throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllAccountInformation());
    }

    @GetMapping("/user-information")
    public ResponseEntity<?> getAccount() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAccountInformation(kcId));
    }

    @GetMapping("/account-cvu")
    public ResponseEntity<?> getAccountByCvu(@PathVariable String cvu) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAccountByUserCvu(cvu));
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getAllTransactions(@RequestParam(required = false) Integer _limit) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        if (_limit != null && _limit == 5) {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.getLastFiveTransactions(userId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllTransactions(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<?> getTransaction(@PathVariable Long activityId) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getTransaction(userId, activityId));
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerNewCard(@Valid @RequestBody CardRequest cardRequest) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountsService.registerCard(cardRequest, userId));
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getAllCards() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllCards(userId));
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<?> getCardById(@PathVariable Long id) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getCardById(id, userId));
    }

    @DeleteMapping("/delete-card/{cardId}")
    public ResponseEntity<?> deleteCardById(@PathVariable Long cardId) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        accountsService.deleteCardByNumber(cardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register-transaction")
    public ResponseEntity<?> registerNewTransaction(@RequestBody TransactionRequest transactionRequest) throws ResourceNotFoundException{
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        try {
            Long response = accountsService.registerTransaction(transactionRequest, userId);
            return ResponseEntity.ok(Collections.singletonMap("id", response));
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (CheckTransactionDataException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
