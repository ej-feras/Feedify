package com.feedify.rest.service.email;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedify.database.entity.User;
import com.feedify.database.entity.tokens.ConfirmationToken;
import com.feedify.database.repository.ConfirmationTokenRepository;
import com.feedify.rest.service.AccountService;

@Service
public class RestorePasswordTokenService {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private AccountService accountService;

	public ConfirmationToken findByConfirmationToken(String confirmationToken) {
		return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
	}

	@Transactional
	public ConfirmationToken save(ConfirmationToken confirmationToken) {
		return confirmationTokenRepository.save(confirmationToken);
	}

	@Transactional
	public void removeUserOfConfirmationToken(Long userId) {
		confirmationTokenRepository.removeUserOfConfirmationToken(userId);
	}

	@Transactional
	public void deleteByUser(User user) {
		confirmationTokenRepository.deleteByUser(user);
	}

	/**
	 *
	 *
	 * @param token
	 *
	 * @author Feras Ejneid
	 */
	public void updateConfirmationToken(ConfirmationToken token) {
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