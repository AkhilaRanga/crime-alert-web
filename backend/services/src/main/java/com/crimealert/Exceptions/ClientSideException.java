package com.crimealert.Exceptions;

public class ClientSideException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	
	public ClientSideException(String message)
	{
		this.message = message;
	}
	
	public String toString()
	{
		return message;
	}

}
