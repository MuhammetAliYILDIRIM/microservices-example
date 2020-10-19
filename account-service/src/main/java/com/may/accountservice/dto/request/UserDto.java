package com.may.accountservice.dto.request;

import com.may.accountservice.util.validator.Email;
import com.may.accountservice.util.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {

    @Email
    private String email;
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Password
    private String password;

}
