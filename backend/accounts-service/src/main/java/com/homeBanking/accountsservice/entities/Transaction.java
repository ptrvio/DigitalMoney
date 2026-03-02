package com.homeBanking.accountsservice.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Transaction {

    private Long id;
    private Long accountId;
    private String type;
    private Double amount;
    private String origin;
    private String destination;
    private String name;
    private String description;
    private LocalDateTime dated;
}
