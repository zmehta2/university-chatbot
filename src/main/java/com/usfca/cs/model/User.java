package com.usfca.cs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * User is the model class for user accounts.
 * 
 * @Author Zinal
 *
 */
@Data
@Document(collection = "users")
public class User {

		@Id
		private String id;
		private String username;
		private String password;
		private String email;
		private String role; // ADMIN, USER
		private boolean active;
	}
