package com.homeBanking.transactionsservice.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private Long accountId;
    private String type;
    private Double amount;
    private String origin;
    private String destination;
    private String name;
    private String description;
    private LocalDateTime dated;

    public TransactionRequest(Long accountId,String type, Double amount, String origin, String destination, String name, String description, LocalDateTime dated) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.description = description;
        this.dated = dated;
    }
}
