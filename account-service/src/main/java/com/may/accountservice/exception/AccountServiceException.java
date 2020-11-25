package com.may.accountservice.exception;

import lombok.Getter;

@Getter
public class AccountServiceException extends RuntimeException {
    private final ErrorType errorType;

    public AccountServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
