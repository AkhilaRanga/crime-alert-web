package com.crimealert.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.Binary;
import org.bson.types.ObjectId;

public class Photo {
	private String _id;
	private String postId;
	
	@NotNull(message = "Title cannot be null")
	@NotEmpty(message = "Title cannot be empty")
	private String title;
	private Binary image;
	private Date timeCreated;

	// _id
	public String getId() {
		return this._id;
	}
	
	public void setId(String _id) {
		this._id = _id;
	}
	
	// postId
	public String getPostId() {
		return this.postId;
	}
	
	public void setPostId(String postId) {
		this.postId = postId;
	}

	// title
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	// image
	public Binary getImage() {
		return this.image;
	}
	
	public void setImage(Binary image) {
		this.image = image;
	}
	
	// timeCreated
	public Date getTimeCreated() {
		return this.timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
}
