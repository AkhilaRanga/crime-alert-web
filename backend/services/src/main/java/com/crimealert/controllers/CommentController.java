package com.crimealert.controllers;

import java.util.List;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.models.Comment;
import com.crimealert.services.CommentService;
import com.crimealert.utils.ValidatorUtil;

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

@Path("comments")
public class CommentController {

	private CommentService commentService;
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createComment(
			Comment comment
	) {
    	try {
    	
    			
        	Document response = getCommentService().createComment(comment);
        		
        	System.out.println("Post Insertion Response : " + response.toJson());
        		
        	return Response.ok(response.toJson()).build();
    		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateComment(
			 Comment comment
	) {
    	try {

    		if ((comment.getComment() != null && !comment.getComment().isEmpty()) && (comment.getId() != null && !comment.getId().isEmpty())) {
    			
        	Document response = getCommentService().updateComment(comment);
        		
        	System.out.println("Post Update Response : " + response.toJson());
        	
        	return Response.ok(response.toJson()).build();
    		} else {
    			return Response.status(400).entity("Comment or Comment Id cannot be empty").build();
    		}
        		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@DELETE
    @Produces(MediaType.APPLICATION_JSON)
	public Response deleteComment(
			@PathParam("commentId")String commentId
	) {
    	try {
    	
    		if (commentId != null && !commentId.isEmpty()){
    			
        	Document response = getCommentService().deleteComment(commentId);
        		
        	System.out.println("Post Deletion Response : " + response.toJson());
        		
        	return Response.ok(response.toJson()).build();
    		}
    		else {
    			return Response.status(400).entity("Comment Id cannot be empty").build();
    		}
    		
    		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response listComments(
			@PathParam("postId")String postId
	) {
    	try {
    	
    			
        	List<Document> response = getCommentService().listComments(postId);
        		
        	System.out.println("List Response : " + response.toString());
        		
        	return Response.ok(response).build();
    		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	public CommentService getCommentService()
	{
		if(commentService == null)
			commentService = new CommentService();
		return commentService;
	}
}