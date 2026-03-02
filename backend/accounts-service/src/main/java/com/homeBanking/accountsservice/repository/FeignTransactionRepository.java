package com.homeBanking.accountsservice.repository;

import com.homeBanking.accountsservice.entities.Transaction;
import com.homeBanking.accountsservice.entities.TransactionRequestWithAccountId;
import com.homeBanking.accountsservice.feignCustomExceptions.CustomErrorDecoder;
import com.homeBanking.accountsservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "transactions-service",
        path = "/transaction",
        configuration = {FeignConfig.class, CustomErrorDecoder.class}
)

public interface FeignTransactionRepository {

    @GetMapping("/lastTransactions/{cvu}")
    List<Transaction> getLastFiveTransactions(@PathVariable String cvu, @RequestParam Long accountId);

    @GetMapping("/getAll/{cvu}")
    List<Transaction> getAllTransactions(@PathVariable String cvu, @RequestParam Long accountId);

    @GetMapping("/{activityId}/account/{accountId}")
    Transaction getTransaction(@PathVariable Long accountId, @PathVariable Long activityId);

    @PostMapping("/register-transaction")
    Long createTransaction(@RequestBody TransactionRequestWithAccountId transaction);
}
