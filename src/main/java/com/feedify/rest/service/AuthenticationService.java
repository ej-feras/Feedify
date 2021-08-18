package com.feedify.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedify.commands.PasswordCommand;
import com.feedify.commands.UserCommand;
import com.feedify.converters.UserCommandToUser;
import com.feedify.database.entity.User;
import com.feedify.database.entity.tokens.Token;
import com.feedify.database.repository.UserRepository;
import com.feedify.exceptions.EmailAlreadyExistException;
import com.feedify.exceptions.UserCouldNotBeSavedException;
import com.feedify.exceptions.UsernameAlreadyExistException;
import com.feedify.rest.service.email.EmailService;
import com.feedify.rest.service.email.TokenService;

import java.util.Optional;

@Service
public class AuthenticationService {

	private final static String EMAIL_EXIST = "There is an account with that email address: ";
	private final static String USERNAME_EXIST = "There is an account with that username : ";
	private final static String USER_NOT_SAVED = "User could not be saved, please try again.";

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCommandToUser userCommandToUser;
	@Autowired
	private EmailService emailService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TokenService tokenService;

	/**
	 * Creates new account.
	 * 
	 * @param userCommand
	 * @return User
	 * @throws EmailAlreadyExistException
	 * @throws UsernameAlreadyExistException
	 */
	public User signUp(UserCommand userCommand) throws EmailAlreadyExistException, UsernameAlreadyExistException {
		if (emailExist(userCommand.getEmail()))
			throw new EmailAlreadyExistException(EMAIL_EXIST + userCommand.getEmail());

		if (usernameExist(userCommand.getUsername()))
			throw new UsernameAlreadyExistException(USERNAME_EXIST + userCommand.getUsername());

		User user = userCommandToUser.convert(userCommand);
//        user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		userRepository.save(user);

		emailService.sendConfirmationEmail(user);

		return user;
	}

	/**
	 * @param email
	 * @return boolean, true wether the email exists
	 */
	private boolean emailExist(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.isPresent();
	}

	/**
	 * @param username
	 * @return boolean, true wether the username exists
	 */
	private boolean usernameExist(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		return user.isPresent();
	}

	public void restorePassword(String email) throws UserCouldNotBeSavedException {
		System.out.println("email " + email);
		Optional<User> user = accountService.findUserByEmail(email);
		if (user.isPresent())
			emailService.sendRestorePasswordEmail(user.get());
	}

	public boolean storePassword(PasswordCommand passwordCommand) {
		String username = passwordCommand.getUsername();
		String passwordToken = passwordCommand.getPasswordToken();

		Token token = tokenService.findByPasswordToken(passwordToken);
		User user = token.getUser();

		if (!user.getUsername().equals(username))
			return false;

		accountService.changePassword(passwordCommand);
		return true;
	}

}
