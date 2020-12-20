package com.may.accountservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.may.accountservice.constants.Constants.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(AccountServiceException.class)
    @Order(HIGHEST_PRECEDENCE)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleAccountServiceException(AccountServiceException exception) {
        return new ResponseEntity<>(createError(exception.getErrorType(), exception),
                exception.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintException(ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);
        final List<ValidationError> validationErrors = new ArrayList<>();

        exception.getConstraintViolations().forEach(env -> {
            final String[] strArr = env.getPropertyPath().toString().split("\\.");
            final String field = strArr[strArr.length - 1];
            final String message = env.getMessage();
            validationErrors.add(ValidationError.builder()
                    .field(field)
                    .message(message)
                    .build());
        });

        List<FieldError> fieldErrors = new ArrayList<>();
        for (ValidationError validationError : validationErrors) {
            fieldErrors.add(new FieldError(validationError.getField(), validationError.getMessage()));
        }

        ErrorMessage errorMessage = new ErrorMessage(CONSTRAINT_VIOLATION_CODE, BAD_REQUEST,
                CONSTRAINT_VIOLATION_MESSAGE, fieldErrors);
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(MESSAGE_NOT_READABLE_CODE, BAD_REQUEST,
                HTTP_MESSAGE_NOT_READABLE_MESSAGE, null);
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = new ArrayList<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> fieldErrors.add(new FieldError(e.getField(), e.getDefaultMessage())));

        ErrorMessage errorMessage = new ErrorMessage(CONSTRAINT_VIOLATION_CODE, BAD_REQUEST,
                CONSTRAINT_VIOLATION_MESSAGE, fieldErrors);
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
