package com.crimealert.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.Binary;

public class Video {
	private String _id;
	private String postId;
	
	@NotNull(message = "Title cannot be null")
	@NotEmpty(message = "Title cannot be empty")
	private String title;
	private String videoIdGridFS;
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

	// videoIdGridFS
	public String getImage() {
		return this.videoIdGridFS;
	}
	
	public void setImage(String videoIdGridFS) {
		this.videoIdGridFS = videoIdGridFS;
	}
	
	// timeCreated
	public Date getTimeCreated() {
		return this.timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
}
