package com.feedify.database.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.feedify.database.entity.User;

import com.feedify.database.entity.tokens.PasswordToken;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
	
	PasswordToken findByResotrePasswordToken(String token);

	@Modifying
	@Query("update PasswordToken ct set ct.user = null where ct.user.id = ?1")
	@Transactional
	void removeUserOfPasswordToken(Long userId);

	 void deleteByUser(User user);
}