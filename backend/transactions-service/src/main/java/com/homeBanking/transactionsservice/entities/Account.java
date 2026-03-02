package com.homeBanking.transactionsservice.entities;


import lombok.Data;


@Data
public class Account {
    private Long id;
    private Long userId;
    private Double balance;


    public Account(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

}
