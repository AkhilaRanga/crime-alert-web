package com.crimealert.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Otp {
	@NotNull(message = "Token cannot be null")
	@NotEmpty(message = "Token cannot be empty")
    private String email;
	
	@NotNull(message = "Token cannot be null")
	@NotEmpty(message = "Token cannot be empty")
	private String oneTimeToken;

	public String getEmail() {
			return this.email;
		}
		
	public void setEmail(String email) {
			this.email = email;
		}
	
	public String getOneTimeToken() {
		return this.oneTimeToken;
	}
	
	public void setOneTimeToken(String oneTimeToken) {
		this.oneTimeToken = oneTimeToken;
	}

}
