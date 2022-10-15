package com.crimealert.models;

public class EncodedPassword {
	private String encodedPassword;
	private String salt;
	
	public String getEncodedPassword()
	{
		return encodedPassword;
	}
	
	public void setEncodedPassword(String encodedPassword)
	{
		this.encodedPassword = encodedPassword;
	}
	
	public String getSalt()
	{
		return salt;
	}
	
	public void setSalt(String salt)
	{
		this.salt = salt;
	}
	
}
