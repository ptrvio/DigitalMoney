package com.homeBanking.transactionsservice.controller;


import com.homeBanking.transactionsservice.entities.Transaction;
import com.homeBanking.transactionsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.transactionsservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/register-transaction")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction saved = transactionService.createTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lastTransactions/{cvu}")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable String cvu, @RequestParam Long accountId) throws ResourceNotFoundException {
        Optional<List<Transaction>> optionalTransactions = transactionService.getLastFiveTransactionsByCvu(cvu,accountId);
        if(optionalTransactions.isPresent()){
            return ResponseEntity.ok().body(optionalTransactions.get());
        }
        return (ResponseEntity<List<Transaction>>) ResponseEntity.notFound();

    }


    @GetMapping("/getAll/{cvu}")
    public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable String cvu, @RequestParam Long accountId) throws ResourceNotFoundException {
        Optional<List<Transaction>> transactionsOptional = transactionService.getAllTransactions(cvu,accountId);
        if(transactionsOptional.isPresent() && !transactionsOptional.get().isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(transactionsOptional.get());
        }
        else {
            throw new ResourceNotFoundException("No se encontraron transacciones para la cuenta con ID " + accountId);
        }
    }

    @GetMapping("/{activityId}/account/{accountId}")
    public ResponseEntity<?> getTransaction(@PathVariable Long accountId, @PathVariable Long activityId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(accountId, activityId));

    }

}
