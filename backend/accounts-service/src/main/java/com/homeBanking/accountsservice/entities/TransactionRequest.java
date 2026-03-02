package com.homeBanking.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TransactionRequest {
    private String type;
    private Double amount;
    private String origin;
    private String destination;
    private String name;
    private String description;
}
