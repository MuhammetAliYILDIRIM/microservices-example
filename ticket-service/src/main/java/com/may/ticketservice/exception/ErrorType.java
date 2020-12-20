package com.may.ticketservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(10001, "An internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    DESCRIPTION_CAN_NOT_BE_EMPTY(10002, "Description cannot be empty", HttpStatus.NOT_ACCEPTABLE),
    TICKET_NOT_FOUND(10003, "Ticket can not be found.", HttpStatus.NOT_FOUND),
    ASSIGNEE_NOT_FOUND(10004, "Assignee can not be found", HttpStatus.BAD_REQUEST);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
