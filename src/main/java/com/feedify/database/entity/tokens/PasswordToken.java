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
public class PasswordToken extends Token {

	@Column(name = "resotre_password_token")
	private String resotrePasswordToken;

	public PasswordToken(User user) {
		super(user);
		this.resotrePasswordToken = UUID.randomUUID().toString();
	}
	
	 public  String getToken() {
		 return resotrePasswordToken;
	 }
}
