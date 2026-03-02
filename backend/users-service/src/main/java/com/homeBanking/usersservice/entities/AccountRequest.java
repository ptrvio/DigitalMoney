package com.homeBanking.usersservice.entities;

import lombok.Data;

@Data
public class AccountRequest {
    private Long userId;

    public AccountRequest(Long userId) {
        this.userId = userId;
    }
}
