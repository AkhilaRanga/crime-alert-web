package com.crimealert.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



public class User {
	@NotNull(message = "Name cannot be null")
	private String fullName;
	
	@Email(message = "Email should be valid")
	private String email;
	
	@NotNull(message = "Location cannot be null")
	private String location;
	
	@Pattern(regexp = "^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$", message = "Phone Number must satisfy the pattern")
	private String phoneNumber;
	
	@NotNull(message = "Password cannot be null")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$" , message = "Pattern must satisfy the pattern - 8characters, one uppercase, one lowercase, one digit, one special character" )
	private String password;
	
	private Boolean enableNotifications;
	
	
	public String getFullName()
	{
		return fullName;
	}
	
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public Boolean getEnableNotifications()
	{
		return enableNotifications;
	}
	
	public void setEnableNotifications(Boolean enableNotifications)
	{
		this.enableNotifications = enableNotifications;
	}	

}
