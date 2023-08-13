package com.crimealert.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

import com.crimealert.enums.CrimeLevelEnum.crimeLevel;

public class Post {
	private String _id;
	private String userId;
	
	@NotNull(message = "Title cannot be null")
	@NotEmpty(message = "Title cannot be empty")
	private String title;

	@NotNull(message = "Description cannot be null")
	@NotEmpty(message = "Description cannot be empty")
	private String description;

	@NotNull(message = "Location cannot be null")
	@NotEmpty(message = "Location cannot be empty")
	private String location;

	private crimeLevel crimeType;
	private boolean isFlagged;
	private int flagsCount;
	private int likesCount;
	private Date timeCreated;
	private Date timeUpdated;

	// _id
	public String getId() {
		return this._id;
	}
	
	public void setId(String _id) {
		this._id = _id;
	}
	
	// userId
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	// title
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	// description
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	// location
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	// crimeType
	public crimeLevel getCrimeType() {
		return this.crimeType;
	}
	
	public void setCrimeType(crimeLevel crimeType) {
		this.crimeType = crimeType;
	}
	
	// isFlagged
	public boolean getIsFlagged() {
		return this.isFlagged;
	}
	
	public void setIsFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}
	
	// flagsCount
	public int getFlagsCount() {
		return this.flagsCount;
	}
	
	public void setFlagsCount(int flagsCount) {
		this.flagsCount = flagsCount;
	}
	
	// likesCount
	public int getLikesCount() {
		return this.likesCount;
	}
	
	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}
	
	// timeCreated
	public Date getTimeCreated() {
		return this.timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	// timeUpdated
	public Date getTimeUpdated() {
		return this.timeUpdated;
	}
	
	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
}
