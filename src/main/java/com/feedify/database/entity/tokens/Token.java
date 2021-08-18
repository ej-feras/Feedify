package com.feedify.database.entity.tokens;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.feedify.database.entity.BaseEntity;
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
public abstract class Token extends BaseEntity {

	@CreationTimestamp
	private LocalDateTime createdDate;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "user_id")
	private User user;

	private boolean used;
	private boolean sent;

	public Token(User user) {
		this.user = user;
	}
	
	 public abstract String getToken();
}
