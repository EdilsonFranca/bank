package com.Bank.Bank.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
	private String name;
	private String email;
	private String token;
	private String type = "Bearer";

	public JwtResponse(String accessToken, String name, String email) {
		this.name = name;
		this.email = email;
		this.token = accessToken;
	}
}