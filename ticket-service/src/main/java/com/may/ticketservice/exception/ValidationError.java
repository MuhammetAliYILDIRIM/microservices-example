package com.may.ticketservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String field;

    private String message;

    public ValidationError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return StringUtils.isEmpty(field) ? String.format("[%s]", message)
                : String.format("[%s]:[%s]", field, message);
    }
}
