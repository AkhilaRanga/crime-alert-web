package com.crimealert.controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.models.Photo;
import com.crimealert.models.Post;
import com.crimealert.models.Video;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

@Path("posts")
@Singleton
public class PostController {
	
	private PostService postService;
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createPost(
			Post post
	) {
    	try {
    		String validateResponse = ValidatorUtil.validateForm(post);
    		
    		System.out.println("Post Validation Response : " + validateResponse);
    		
    		if (validateResponse.isEmpty()) {
    			
        		Document response = getPostService().createPost(post);
        		
        		System.out.println("Post Insertion Response : " + response.toJson());
        		
        		return Response.ok(response.toJson()).build();
    		} else {
    			return Response.status(400).entity(validateResponse).build();
    		}
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	@Path("uploadPhoto")
	public Response uploadPhoto(
			@FormDataParam("postId") String postId,
			@FormDataParam("file") FormDataBodyPart body
	) {
		//need to check if photos can be uploaded if file size are big
		System.out.println("Uploading Photos for post : " + postId);
		
		List<Photo> photos = null;
    	try {
    		
        		if(!postId.isBlank() && body != null)
	    		{
        			List<InputStream> images  = new ArrayList<>();
        			
            		List<ContentDisposition> cd = new ArrayList<>();
            		
        			for(BodyPart part : body.getParent().getBodyParts()){
        				
        		        InputStream is = part.getEntityAs(InputStream.class);
        		        ContentDisposition meta = part.getContentDisposition();
        		        images.add(is);
        		        cd.add(meta);
        			}

        			photos = getPostService().createPhotos(postId, images, cd);
	    		}

	    		if (photos!=null && photos.size() > 0)
	    			return Response.ok("Photos Uploaded").build();
	    		else
	    			return Response.status(400).entity("Photos not uploaded").build();
        		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	@Path("uploadVideo")
	public Response uploadVideo(
			@FormDataParam("postId") String postId,
			@FormDataParam("video") FormDataBodyPart bodyVideo,
			@FormDataParam("videoName") String videoName
	) {
		System.out.println("Uploading Video for post : " + postId);
		
		List<Video> videos = null;
    	try {
    		
    		if(postId != null && bodyVideo != null)
    		{
    			List<InputStream> videosInp  = new ArrayList<>();
        		
    			for(BodyPart part : bodyVideo.getParent().getBodyParts()){
    				
    		        InputStream is = part.getEntityAs(InputStream.class);
    		        videosInp.add(is);
    			}
    			
    			 videos = getPostService().createVideos(postId, videosInp, videoName);

    		}

	    		if (videos != null && videos.size() != 0)
	    			return Response.ok("Video Uploaded").build();
	    		else
	    			return Response.status(400).entity("Video not uploaded").build();
        		
    	} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listPosts(@QueryParam("location") String location) {
    	try {
    		List<Document> response = postService.listPosts(location);
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
	@Path("getPost")
	public Response getPost(@QueryParam("postId")String postId) {
    	try {
    		Document response = getPostService().getPost(postId);
    		if(response != null)
    		    return Response.ok(response.toJson()).build();
    		else
        		return Response.ok("Post not found").build();
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
	@Path("getPhoto")
	public Response getPhoto(@QueryParam("photoId")String photoId) {
    	try {
    		Document photoResponse = getPostService().getPhoto(photoId);
    		return Response.ok(photoResponse.toJson()).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("downloadPhoto")
	public Response downloadPhoto(@PathParam("photoId")String photoId) {
    	try {
    		Document photoResponse = getPostService().getPhoto(photoId);
    		
    		String imageName = photoResponse.get("title").toString();
    		
    		Binary image = photoResponse.get("image", org.bson.types.Binary.class);
    		
    		StreamingOutput streamingOutputImage = output -> {
                byte[] data = image.getData();
                output.write(data);
            };
    		
    		return Response.ok(streamingOutputImage, MediaType.APPLICATION_OCTET_STREAM)
    				.header("Content-Disposition", "attachment; filename=\"" + imageName + "\"")
    				.build();
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
	@Path("getVideo")
	public Response getVideo(String videoId) {
    	try {
    		Document videoResponse = getPostService().getVideo(videoId);
    		return Response.ok(videoResponse.toJson()).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("downloadVideo")
	public Response downloadVideo(@PathParam("videoId")String videoId) {
    	try {
    		Document video = getPostService().getVideo(videoId);
    		
    		String videos = video.get("videoIdGridFS").toString();
    		
    		StreamingOutput streamingOutputVideo = getPostService().downloadVideo(new ObjectId(videos));
    		
    		return Response.ok(streamingOutputVideo, MediaType.APPLICATION_OCTET_STREAM)
    				.header("Content-Disposition", "attachment; filename=\"" + video.get("title") + "\"")
    				.build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/{postId}")
	public Response updatePost(@PathParam("postId") String postId, Post post) {
    	try {
    		String validateResponse = ValidatorUtil.validateForm(post);
    		if (validateResponse.isEmpty()) {
    			Document response = getPostService().updatePost(post, postId);
        		System.out.println("Post Updation Response : " + response.toJson());
        		return Response.ok(response.toJson()).build();
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
	@Path("deletePhoto/{photoId}")
	public Response deletePhoto(@PathParam("photoId") String photoId) {
    	try {
    		String response = getPostService().deletePhoto(photoId);
    		System.out.println("Photo Deletion Response : " + photoId + " " + response);
    		return Response.ok(response).build();
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
	@Path("deleteVideo/{videoId}")
	public Response deleteVideo(@PathParam("videoId") String videoId) {
    	try {
    		String response = getPostService().deleteVideo(videoId);
    		System.out.println("Video Deletion Response : " + videoId + " " + response);
    		return Response.ok(response).build();
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
	@Path("/{postId}/{userId}")
	public Response deletePost(@PathParam("postId") String postId, @PathParam("userId") String userId) {
    	try {
    		String response = getPostService().deletePost(postId, userId);
    		System.out.println("Post Deletion Response : " + response);
    		return Response.ok(response).build();
    	} catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	public PostService getPostService()
	{
		if(postService == null)
			postService = new PostService();
		return postService;
	}
}
