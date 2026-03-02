package com.homeBanking.transactionsservice.service;


import com.homeBanking.transactionsservice.entities.Transaction;
import com.homeBanking.transactionsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.transactionsservice.repository.FeignUserRepository;
import com.homeBanking.transactionsservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private FeignUserRepository feignUserRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        transaction.setDated(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Optional<List<Transaction>> getLastFiveTransactionsByCvu(String cvu,Long accountId) throws ResourceNotFoundException {
        Pageable topFive = PageRequest.of(0, 5);
        List<Transaction> lastFiveTransactions = transactionRepository.getLastFiveTransactions(cvu, accountId, topFive);

        if(lastFiveTransactions==null || lastFiveTransactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found");
        }

        for (Transaction t : lastFiveTransactions) {
            if ("Transfer".equalsIgnoreCase(t.getType())) {
                if (cvu.equals(t.getOrigin())) {
                    t.setAmount(-Math.abs(t.getAmount()));
                }
                else if (cvu.equals(t.getDestination())) {
                    t.setAmount(Math.abs(t.getAmount()));
                    Long userIdDestination = feignUserRepository.getUserIdByCvu(t.getOrigin());
                    String name = feignUserRepository.getNameByUserId(userIdDestination);
                    t.setName(name);
                }
            }
        }
        return Optional.of(lastFiveTransactions);
    }

    public Optional<List<Transaction>> getAllTransactions(String cvu,Long accountId) throws ResourceNotFoundException {

        List<Transaction> lastFiveTransactions = transactionRepository.getAllTransactions(cvu, accountId);

        if(lastFiveTransactions==null || lastFiveTransactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found");
        }

        for (Transaction t : lastFiveTransactions) {
            if ("Transfer".equalsIgnoreCase(t.getType())) {
                if (cvu.equals(t.getOrigin())) {
                    t.setAmount(-Math.abs(t.getAmount()));
                }
                else if (cvu.equals(t.getDestination())) {
                    t.setAmount(Math.abs(t.getAmount()));
                    Long userIdDestination = feignUserRepository.getUserIdByCvu(t.getOrigin());
                    String name = feignUserRepository.getNameByUserId(userIdDestination);
                    t.setName(name);
                }
            }
        }
        return Optional.of(lastFiveTransactions);
    }

    public Transaction getTransactionById(Long accountId, Long activityId) throws ResourceNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findTransaction(accountId, activityId);
        if(transactionOptional.isEmpty()) {
            throw new ResourceNotFoundException("No transaction was found with the provided id");
        } else {
            return transactionOptional.get();
        }
    }
}
