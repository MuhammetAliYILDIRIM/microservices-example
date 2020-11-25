package com.may.accountservice.dto.request;

import com.may.accountservice.util.validator.Email;
import com.may.accountservice.util.validator.Password;
import com.may.accountservice.util.validator.PhoneNumber;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountDto {

    @Email
    private String email;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Password
    private String password;

    @PhoneNumber
    private String phoneNumber;

    @NotBlank(message = "Address line is mandatory")
    private String addressLineOne;

    private String addressLineTwo;

    @NotBlank(message = "Country is mandatory")
    private String country;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Town is mandatory")
    private String town;

    @NotBlank(message = "Post code is mandatory")
    private String postCode;
}
