package com.crimealert.controllers;

import java.io.InputStream;

import org.bson.Document;
import org.bson.types.Binary;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.models.Post;
import com.crimealert.services.PostService;
import com.crimealert.utils.ValidatorUtil;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("posts")
@Singleton
public class PostController {
	@Singleton
	private PostService postService;
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createPost(
			@FormDataParam("post") Post post,
			@FormDataParam("image") Binary[] images,
			@FormDataParam("video") InputStream[] videos
	) {
    	try {
    		String validateResponse = ValidatorUtil.validateForm(post);
    		if (validateResponse.isEmpty()) {
        		Document response = postService.createPost(post);
        		return Response.ok(response).build();
    		} else {
    			return Response.status(400).entity(validateResponse).build();
    		}
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listPosts() {
    	try {
    		Document[] response = postService.listPosts();
    		return Response.ok(response).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	 
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getPost(@PathParam("postId") String postId) {
    	try {
    		Document response = postService.getPost(postId);
    		return Response.ok(response).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updatePost(
			@PathParam("postId") String postId,
			@FormDataParam("post") Post post,
			@FormDataParam("image") Binary[] images,
			@FormDataParam("video") InputStream[] videos
	) {
    	try {
    		String validateResponse = ValidatorUtil.validateForm(post);
    		if (validateResponse.isEmpty()) {
    			Document response = postService.updatePost(postId, post);
        		return Response.ok(response).build();
    		} else {
    			return Response.status(400).entity(validateResponse).build();
    		}
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@DELETE
    @Produces(MediaType.TEXT_PLAIN)
	public Response deletePost(@PathParam("postId") String postId) {
    	try {
    		String response = postService.deletePost(postId);
    		return Response.ok(response).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
}
