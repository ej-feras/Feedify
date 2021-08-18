package com.feedify.rest.service.email;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedify.database.entity.User;
import com.feedify.database.entity.tokens.ConfirmationToken;
import com.feedify.database.entity.tokens.PasswordToken;
import com.feedify.database.entity.tokens.Token;
import com.feedify.database.repository.ConfirmationTokenRepository;
import com.feedify.database.repository.PasswordTokenRepository;
import com.feedify.rest.service.AccountService;

@Service
public class TokenService {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private PasswordTokenRepository passwordTokenRepository;

	@Autowired
	private AccountService accountService;

	public Token findByConfirmationToken(String confirmationToken) {
		return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
	}
	
	public Token findByPasswordToken(String passwordToken) {
		return passwordTokenRepository.findByResotrePasswordToken(passwordToken);
	}


	@Transactional
	public Optional<Token> save(Token token) {
		if (token instanceof ConfirmationToken) {
			return Optional.of(confirmationTokenRepository.save((ConfirmationToken) token));
		} else if (token instanceof PasswordToken) {
			return Optional.of(passwordTokenRepository.save((PasswordToken) token));
		}

		return Optional.empty();
	}

	@Transactional
	public void removeUserOfConfirmationToken(Long userId) {
		confirmationTokenRepository.removeUserOfConfirmationToken(userId);
	}
	
	@Transactional
	public void removeUserOfPasswordToken(Long userId) {
		passwordTokenRepository.removeUserOfPasswordToken(userId);
	}

	@Transactional
	public void deleteConfirmationTokenByUser(User user) {
		confirmationTokenRepository.deleteByUser(user);
	}
	
	@Transactional
	public void deletePasswordTokenByUser(User user) {
		passwordTokenRepository.deleteByUser(user);
	}

	/**
	 *
	 *
	 * @param token
	 *
	 * @author Feras Ejneid
	 */
	public void updateConfirmationToken(Token token) {
		Optional<User> optionalUser = accountService.findUserByEmail(token.getUser().getEmail());

		if (!optionalUser.isPresent())
			return;

		User user = optionalUser.get();
		user.setEnabled(true);
		accountService.saveUser(user);
		token.setUsed(true);
		save(token);
	}

}