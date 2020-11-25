package com.may.ticketservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketServiceException.class)
    @Order(HIGHEST_PRECEDENCE)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleTicketServiceException(TicketServiceException exception) {
        return new ResponseEntity<>(createError(exception.getErrorType(), exception),
                exception.getErrorType().getHttpStatus());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(10000, BAD_REQUEST,
                "Http Message Not Readable");
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {

        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    private ErrorMessage createError(ErrorType errorType, Exception exception) {
        log.error("Error occurred. {}", exception.getMessage());

        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .httpStatus(errorType.getHttpStatus())
                .build();
    }

}
