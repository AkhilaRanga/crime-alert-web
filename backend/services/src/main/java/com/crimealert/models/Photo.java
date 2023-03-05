package com.crimealert.models;

import org.bson.types.Binary;
import org.bson.types.ObjectId;

public class Photo {
	
	private ObjectId photoId;
    
    private String title;
        
    private Binary image;
    
    private ObjectId postId;
    
    public ObjectId getPhotoId()
	{
		return photoId;
	}
	
	public void setPhotoId(ObjectId photoId)
	{
		this.photoId = photoId;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public Binary getImage()
	{
		return image;
	}
	
	public void setImage(Binary image)
	{
		this.image = image;
	}
	
	public ObjectId getPostId()
	{
		return postId;
	}
	
	public void setPostId(ObjectId postId)
	{
		this.postId = postId;
	}

}
