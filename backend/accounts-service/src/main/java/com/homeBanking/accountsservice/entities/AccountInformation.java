package com.homeBanking.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInformation {
    private Double balance;
    private String cvu;
    private String alias;
    private String userId;
    private String id;
    private String name;
}
