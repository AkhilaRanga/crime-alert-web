package com.crimealert.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Comment {
	private String _id;

	@NotNull(message = "userId cannot be null")
	@NotEmpty(message = "userId cannot be empty")
	private String userId;
	
	@NotNull(message = "postId cannot be null")
	@NotEmpty(message = "postId cannot be empty")
	private String postId;

	@NotNull(message = "Comment cannot be null")
	@NotEmpty(message = "Comment cannot be empty")
	private String comment;
	
	private String parentId;

	private boolean isFlagged;
	
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
	
	public String getParentId() {
		return this.parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	// userId
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	// postId
	public String getPostId() {
		return this.postId;
	}
	
	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	// comment
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	// isFlagged
	public boolean getIsFlagged() {
		return this.isFlagged;
	}
	
	public void setIsFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
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
