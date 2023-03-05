package com.crimealert.controllers;

import java.io.File;
import java.io.FileInputStream;

import org.bson.BsonBinarySubType;
import org.bson.Document;
import org.bson.types.Binary;

import com.crimealert.models.Post;
import com.crimealert.models.Photo;
import com.crimealert.services.PostService;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("posts")
@Singleton
public class PostController {
	
	
	@Singleton
	private PostService postService;
	
	@POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPosts(@FormParam(value = "post") Post post, @FormParam(value = "photo")File  photofile) {
		Photo photoResponse = null;
    	try
    	{
    		
	    		Post postResponse= postService.createPosts(post); // need to design return type
	    		
	    		System.out.println("Response received:" + postResponse.getPostId().toString());
	    		
	    		if(postResponse.getPostId() != null && photofile != null)
	    		{
	    			Photo photo = new Photo();
	    			byte[] bytes = new byte[(int) photofile.length()];
	    			 FileInputStream fis = null;
	    		      try {

	    		          fis = new FileInputStream(photofile);

	    		          //read file into bytes[]
	    		          fis.read(bytes);

	    		      } finally {
	    		          if (fis != null) {
	    		              fis.close();
	    		          }
	    		      }
	    			photo.setImage(new Binary(BsonBinarySubType.BINARY, bytes));
	    			photo.setTitle(photofile.getName());
	    			photo.setPostId(postResponse.getPostId());
	    			photoResponse = postService.createPhotos(photo);
	    		}
	    		
	    		
	    		
	    		return Response.ok(photoResponse).build();
    	
    	}catch(Exception ex)
    	{
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }
	
	@GET
    @Path("getPost")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPosts(Post post) {
    	try
    	{
    		
	    		Document postResponse= postService.getPost(post); // need to design return type
	    		
	    		System.out.println("Response received:" + postResponse.toString());
	    		
	    		
	    		return Response.ok(postResponse).build();
    	
    	}catch(Exception ex)
    	{
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }

}
