package com.homeBanking.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class CardResquestWithAccountId {
    private Long accountId;
    private String holder;
    private String number;
    private LocalDate expirationDate;
    private String cvv;
}

