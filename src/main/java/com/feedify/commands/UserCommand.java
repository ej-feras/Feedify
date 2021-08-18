package com.feedify.commands;

import javax.validation.constraints.Email;

import com.feedify.validators.annotation.ValidFormInput;
import com.feedify.validators.annotation.ValidPassword;

import lombok.Data;

@Data
public class UserCommand {

    @ValidFormInput(minLength = 3, maxLength = 60, blank = false, containsOnlyLettersAndNumbers = true)
    private String firstName;

    @ValidFormInput(minLength = 3, maxLength = 60, blank = false, containsOnlyLettersAndNumbers = true)
    private String lastName;

    @Email
    private String email;

    @ValidFormInput(minLength = 5, maxLength = 30, blank = false, containsOnlyLettersAndNumbers = true)
    private String username;

    @ValidPassword
    private String password;
}
