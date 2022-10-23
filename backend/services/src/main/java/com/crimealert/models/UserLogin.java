package com.crimealert.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

public class UserLogin {
	
	@NotNull(message = "Email id is required for login")
	@NotEmpty(message = "Email id is required for login")
	private String email;
	
	@NotNull(message = "Password is required for login")
	@NotEmpty(message = "Password is required for login")
	private String password;
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public String getEmail () {
		return this.email;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public String getPassword () {
		return this.password;
	}
}
