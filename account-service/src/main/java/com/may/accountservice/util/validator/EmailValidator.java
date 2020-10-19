package com.may.accountservice.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.equals("")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email is mandatory.").addConstraintViolation();
            return false;
        }
        String patterns = "^(([A-Za-z0-9]+_?)|([A-Za-z0-9]+\\-?)|([A-Za-z0-9]+\\.?))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\" +
                ".))*\\w{1,63}\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
