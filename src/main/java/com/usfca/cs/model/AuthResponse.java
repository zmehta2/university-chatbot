package com.usfca.cs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
	private String token;
	private String type = "Bearer";

	public AuthResponse(String token) {
		this.token = token;
	}
}
