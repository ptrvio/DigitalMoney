package com.homeBanking.accountsservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckTransactionDataException extends RuntimeException {
    public CheckTransactionDataException(String message) {
        super(message);
    }
}
