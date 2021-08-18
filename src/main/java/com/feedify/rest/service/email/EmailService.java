package com.feedify.rest.service.email;

import org.springframework.stereotype.Service;

import com.feedify.database.entity.User;
import com.feedify.database.entity.tokens.ConfirmationToken;
import com.feedify.database.entity.tokens.PasswordToken;
import com.feedify.database.entity.tokens.Token;
import com.feedify.rest.service.AccountService;

import static com.feedify.constants.Constants.*;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${EMAIL:NOT_FOUND}")
	private String fromEmail;

	@Autowired
	private TokenService tokenService;

	@Autowired(required = false)
	private JavaMailSender emailSender;

	public boolean sendConfirmationEmail(User reciever) {

		if (reciever == null)
			return false;

		Token confirmationToken = new ConfirmationToken(reciever);

		String emailBody = CONFIRMATION_MESSAGE + confirmationToken.getToken();
		
		try {
			sendEmail(reciever.getEmail(), "Complete Registration!", emailBody);
			confirmationToken.setSent(true);
			tokenService.save(confirmationToken);
		} catch (MailException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public boolean sendRestorePasswordEmail(User reciever) {

		if (reciever == null)
			return false;

		Token passwordToken = new PasswordToken(reciever);

		String emailBody = RESET_PASSWORD_MESSAGE + passwordToken.getToken();
		try {
			sendEmail(reciever.getEmail(), "Reset password!", emailBody);
			passwordToken.setSent(true);
			tokenService.save(passwordToken);

		} catch (MailException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	private boolean sendEmail(String to, String subject, String messageText) throws Exception {
		boolean sent = false;
		if (fromEmail.equals("NOT_FOUND"))
			return sent;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(messageText);
		message.setSentDate(new Date());
		if (emailSender != null) {
			emailSender.send(message);
			sent = true;
		}

		return sent;
	}

}
