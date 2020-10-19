package com.may.accountservice.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.equals("")) {
            return false;
        }

        if (password.length() < 8) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must be minimum 8 characters long.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
