package com.crimealert.models;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

public class Post {

	private Long userId;
	
	private ObjectId postId;
	
	@NotNull(message = "Description cannot be null")
	@NotEmpty(message = "Description cannot be empty")
	private String description;
	
	@NotNull(message = "Title cannot be null")
	@NotEmpty(message = "Title cannot be empty")
	private String title;
	
	@NotNull(message = "Location cannot be null")
	@NotEmpty(message = "Location cannot be empty")
	private String location;
	
	@NotNull(message = "Type of Crime cannot be null")
	@NotEmpty(message = "Type of Crime cannot be empty")
	private String typeOfCrime;
	
	private Boolean flagOption;
	
	private Long likes;
	
	private Date timeCreated;
	
	private Date timeUpdated;
	
	
	
	
	public Long getUserId()
	{
		return userId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public ObjectId getPostId()
	{
		return postId;
	}
	
	public void setPostId(ObjectId postId)
	{
		this.postId = postId;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTypeOfCrime()
	{
		return typeOfCrime;
	}
	
	public void setTypeOfCrime(String typeOfCrime)
	{
		this.typeOfCrime = typeOfCrime;
	}
	
	public Boolean getFlagOption()
	{
		return flagOption;
	}
	
	public void setFlagOption(Boolean flagOption)
	{
		this.flagOption = flagOption;
	}
	
	public Long getLikes()
	{
		return likes;
	}
	
	public void setLikes(Long likes)
	{
		this.likes = likes;
	}
	
	public Date getTimeCreated()
	{
		return timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated)
	{
		this.timeCreated = timeCreated;
	}
	
	public Date getTimeUpdated()
	{
		return timeUpdated;
	}
	
	public void setTimeUpdated(Date timeUpdated)
	{
		this.timeUpdated = timeUpdated;
	}

}
