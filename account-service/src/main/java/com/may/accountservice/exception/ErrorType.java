package com.may.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(10001,"An internal error occurred",HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_IN_USE(10002,"The username has been already in use.",HttpStatus.NOT_ACCEPTABLE),
    EMAIL_IN_USE(10002,"The email address has been already in use.",HttpStatus.NOT_ACCEPTABLE),
    USER_NOT_FOUND(10003,"User not found.", BAD_REQUEST);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
