package com.may.ticketservice.exception;

import lombok.Getter;

@Getter
public class TicketServiceException extends RuntimeException {
    private final ErrorType errorType;

    public TicketServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
