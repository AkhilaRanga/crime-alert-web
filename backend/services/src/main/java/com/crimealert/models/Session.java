package com.crimealert.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Session {
	private String _id;
	private String sessionToken;
	
	@NotNull(message = "User id cannot be null")
	@NotEmpty(message = "User id cannot be empty")
	private String userId;

	private Date timeCreated;
	
	// _id
	public String getId() {
		return this._id;
	}
	
	public void setId(String _id) {
		this._id = _id;
	}
	
	// sessionToken
	public String getSessionToken() {
		return this.sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	// userId
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	// timeCreated
	public Date getTimeCreated() {
		return this.timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
}
