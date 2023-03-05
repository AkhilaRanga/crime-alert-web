package com.crimealert.models;

import java.util.Date;

public class MultiMedia {
	
	private Long postId;
	
	private Date timeCreated;
	
	public Long getPostId()
	{
		return postId;
	}
	
	public void setPostId(Long postId)
	{
		this.postId = postId;
	}
	
	public Date getTimeCreated()
	{
		return timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated)
	{
		this.timeCreated = timeCreated;
	}
	

}
