package com.feedify.commands;

import com.feedify.validators.annotation.ValidPassword;

import lombok.Data;

@Data
public class PasswordCommand {

	private String username;

	private String passwordToken;

	@ValidPassword
	private String password;
}
