package com.usfca.cs.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usfca.cs.model.User;
import com.usfca.cs.repository.UserRepository;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public User registerUser(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			if (userRepository.findByUsername(user.getUsername()).get().isActive()) {
				throw new RuntimeException("Username already exists");
			}
//			throw new RuntimeException("Username already exists");
		}

		// Check if email already exists
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Set default role if not specified
		if (user.getRole() == null) {
			user.setRole("USER");
		}

		// Set account as active
		user.setActive(true);

		return userRepository.save(user);
	}

	public String authenticate(String username, String password) {
		Optional<User> userOpt = userRepository.findByUsername(username);

		if (userOpt.isPresent()) {
			throw new RuntimeException("User not found");
		}

		User user = userOpt.get();

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid password");
		}

		if (!user.isActive()) {
			throw new RuntimeException("Account is not active");
		}

		return generateToken(user);
	}

	public User getUserById(String id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User updateUser(User user) {
		if (!userRepository.existsById(user.getId())) {
			throw new RuntimeException("User not found");
		}
		return userRepository.save(user);
	}

	public void deactivateUser(String id) {
		User user = getUserById(id);
		user.setActive(false);
		userRepository.save(user);
	}

	public void changePassword(String userId, String oldPassword, String newPassword) {
		User user = getUserById(userId);

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new RuntimeException("Invalid old password");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	public boolean validateToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
			JwtParser jwtParser = Jwts.parser().verifyWith((SecretKey) key).build();
			jwtParser.parse(token);
			return true;
		} catch (Exception e) {
			System.out.println("Token validation error: " + e.getMessage());
			return false;
		}
	}

	private String generateToken(User user) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		Instant now = Instant.now();

		return Jwts.builder().header().type("JWT").and().issuer("university-faq-chatbot").subject(user.getId())
				.claim("username", user.getUsername()).claim("role", user.getRole()).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(jwtExpiration, ChronoUnit.MILLIS))).signWith(key).compact();
	}
}