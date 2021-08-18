package com.feedify.database.entity.tokens;



import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.feedify.database.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken extends Token {

	@Column(name = "confirmation_token")
	private String confirmationToken;

	public ConfirmationToken(User user) {
		super(user);
		this.confirmationToken = UUID.randomUUID().toString();
	}
	
	 public  String getToken() {
		 return confirmationToken;
	 }
}
