package com.crimealert.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.conversions.Bson;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.PhotoConstant;
import com.crimealert.constants.PostConstant;
import com.crimealert.constants.VideoConstant;
import com.crimealert.models.Photo;
import com.crimealert.models.Post;
import com.crimealert.models.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.model.Filters;

public class PostService {
	
	private DBConnectionService dbConnectionService;
	
	public Document createPost(Post post) {
			Document document;
	        try {

	        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

	            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
	        	//Preparing a document
	            document = preparePostInsert(post);
	            //Inserting the document into the collection
	            //System.out.println("Prepared document for post");
	            database.getCollection(PostConstant.COLLECTION).insertOne(document);
	            //System.out.println("Inserted document for post");
	            post.setId(document.getObjectId("_id").toString());

	            getDBConnectionService().closeDBConnection();

	            System.out.println("Post inserted successfully");
	            
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return document; // return post id
	}
	
	public List<Photo> createPhotos(String postId, List<InputStream> images, List<ContentDisposition> fileDetail) throws IOException
	{ 
		List<Photo> photos = new ArrayList<>();
		int i = 0;
		for(InputStream fs : images)
		{
			byte[] byteArray = null;
			System.out.println("Image uploaded");

		      try {

		         
		    	  
		          //read file into bytes[]
		    	  byteArray = fs.readAllBytes();
		    	  System.out.println("byteArray:"+byteArray);

		      }
		      catch(IOException ioe)
		      {
		    	  System.err.println("An error occurred" + ioe);
		    	  throw ioe;
		      }
		    Photo photo = new Photo();
			photo.setImage(new Binary(BsonBinarySubType.BINARY, byteArray));
			photo.setTitle(fileDetail.get(i).getFileName());
			photo.setPostId(postId);
			Photo photoResponse = insertPhotos(photo);
			
			System.out.println("PhotoResponse:"+photoResponse);
			
			photos.add(photoResponse);	
			i++;
		}
		
		return photos;
	}
	
	public List<Video> createVideos(String postId, List<InputStream> inpVideos, String fileName) throws IOException
	{
		List<Video> videos = new ArrayList<>();
		GridFSBucket bucket = GridFSBuckets.create(
				 getDBConnectionService().getDBConnection().getDatabase(PhotoConstant.DB),
				  "videos");
		
		System.out.println("Size of video"+ inpVideos.size()); // video is divided into three input stream objects but file name is one need to check this issue
		
		for(int i = 0; i < inpVideos.size() ; i ++)
		{
			
			GridFSUploadStream uploadStream = bucket.openUploadStream(fileName);
			byte[] data = null;
			data = inpVideos.get(i).readAllBytes();
			uploadStream.write(data) ;
			
			ObjectId fileId = uploadStream.getObjectId() ;
			uploadStream.close() ; 
			Video video = new Video();
			video.setTitle(fileName);
			video.setPostId(postId);
			video.setVideoId(fileId.toString());
			Video videoResponse = insertVideos(video);
			videos.add(videoResponse);
		}
		
		return videos;
	}
		
	private Document preparePostInsert(Post post)
	{
		Document document = new Document();
	    document.append(PostConstant.USER_ID, post.getUserId());
	    document.append(PostConstant.DESCRIPTION, post.getDescription());
	    document.append(PostConstant.LOCATION, post.getLocation());
	    document.append(PostConstant.TITLE, post.getTitle());
	    document.append(PostConstant.CRIME_TYPE, post.getCrimeType().toString());
	    document.append(PostConstant.IS_FLAGGED, post.getIsFlagged());
	    document.append(PostConstant.LIKES_COUNT, post.getLikesCount());
	    document.append(PostConstant.TIME_CREATED, new Date());	

	    return document;
	}
	
	private Photo insertPhotos(Photo photo)
	{
		try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);

            Document document = new Document();

        	//Preparing a document
            document.append(PhotoConstant.POST_ID, photo.getPostId());
            document.append(PhotoConstant.TITLE, photo.getTitle());
            document.append(PhotoConstant.IMAGE, photo.getImage());
            document.append(PhotoConstant.TIME_CREATED, new Date());

            //Inserting the document into the collection
            database.getCollection(PhotoConstant.COLLECTION).insertOne(document);

            photo.setId(document.getObjectId("_id").toString());
;

            getDBConnectionService().closeDBConnection();

            System.out.println("Photo inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }
        return photo;
		
	}
	
	private Video insertVideos(Video video)
	{
		try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(VideoConstant.DB);

            Document document = new Document();

        	//Preparing a document
            document.append(VideoConstant.POST_ID, video.getPostId());
            document.append(VideoConstant.TITLE, video.getTitle());
            document.append(VideoConstant.VIDEO_ID_GRIDFS, video.getVideoId());
            document.append(VideoConstant.VIDEO_ID_GRIDFS, video.getVideoId());
            document.append(VideoConstant.TIME_CREATED, new Date());

            //Inserting the document into the collection
            database.getCollection(VideoConstant.COLLECTION).insertOne(document);

            video.setId(document.getObjectId("_id").toString());
;

            getDBConnectionService().closeDBConnection();

            System.out.println("Video inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }
        return video;
		
	}
	
	
	
	public Document[] listPosts() {
		try {
			//
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return new Document[1];
	}

	public Document getPost(String postId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
            
            MongoCollection<Document> collection = database.getCollection(PostConstant.COLLECTION);

            Bson filter = Filters.eq("_id", new ObjectId(postId));

            Document document =  collection.find(filter).first();

            return document;
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	}
	
	public Document getPhoto(String photoId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(PhotoConstant.DB);
            
            Bson filter = Filters.eq("_id", new ObjectId(photoId));

            Document document =  database.getCollection(PhotoConstant.COLLECTION).find(filter).first();

            return document;
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	}
	
	public Document getVideo(String videoId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(VideoConstant.DB);

            Bson filter = Filters.eq("_id", new ObjectId(videoId));

            Document document =  (Document) database.getCollection(VideoConstant.COLLECTION).find(filter).first();
            

            return document;
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	}
	
	public Document updatePost(String postId, Post post) {
		try {
			//
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return new Document();
		
	}

	public String deletePost(String postId) {
		try {
			//
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return "Post deleted successfully" + postId;
	}

	public DBConnectionService getDBConnectionService()
	{
		if(dbConnectionService == null)
			dbConnectionService = new DBConnectionService();
		return dbConnectionService;
	}
}
