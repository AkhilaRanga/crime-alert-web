package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Sorts.descending;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.crimealert.constants.UserConstant;
import com.crimealert.constants.VideoConstant;
import com.crimealert.models.Photo;
import com.crimealert.models.Post;
import com.crimealert.models.User;
import com.crimealert.models.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Projections.*;
import java.util.Arrays;

import jakarta.ws.rs.core.StreamingOutput;

public class PostService {
	
	private DBConnectionService dbConnectionService;
	private UserLoginService userLoginService;
	
	public Document createPost(Post post) {
			Document document;
	        try {
	        	Document userLoggedIn = getUserLoginService().searchSession(post.getUserId());
				
				if(userLoggedIn == null)
					throw new ClientSideException("User is not logged In");

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
		      
		    if(byteArray != null)
		    {
		    Photo photo = new Photo();
			photo.setImage(new Binary(BsonBinarySubType.BINARY, byteArray));
			photo.setTitle(fileDetail.get(0).getFileName());
			photo.setPostId(postId);
			Photo photoResponse = insertPhotos(photo);
			System.out.println("PhotoResponse:"+photoResponse);
			photos.add(photoResponse);
		    }
			
				
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
			System.out.println("Inside insertPhotos");

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
	
	
	
	public List<Document> listPosts(String location, String userId) {
		
		List<Document> posts = new ArrayList<>();
		
		try {
			//
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
            
            MongoCollection<Document> collection = database.getCollection(PostConstant.COLLECTION);

            Bson filter = (location != null) ? Filters.eq("location", location) : Filters.eq("userId", userId);
            Document sort = new Document(PostConstant.TIME_CREATED, -1);
            
            AggregateIterable<Document> results = collection.aggregate(Arrays.asList(
                    new Document("$match", filter),
                    new Document("$sort", sort),
                    new Document("$addFields", new Document("string_id", new Document("$toString", "$_id")))
                ));
            for (Document doc : results) {
            	posts.add(doc);
            }           
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
		
		return posts;
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
	
	public List<String> getPhotos(String postId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(PhotoConstant.DB);
            
            Bson filter = Filters.eq(PhotoConstant.POST_ID, postId);

            FindIterable<Document> document =  database.getCollection(PhotoConstant.COLLECTION).find(filter);
            
            Iterator<Document> it = document.iterator();
            
            List<String> photoIds = new ArrayList<>();
			
			while(it.hasNext())
			{
				Document doc = it.next();
				photoIds.add(doc.get("_id").toString());
				System.out.println(doc.get("_id").toString());
				
			}
			
			mongoClient.close();
            
            return photoIds;
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	}
	
	public List<String> getVideos(String postId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(VideoConstant.DB);
            
            Bson filter = Filters.eq(VideoConstant.POST_ID, postId);

            FindIterable<Document> document =  database.getCollection(VideoConstant.COLLECTION).find(filter);
            
            Iterator<Document> it = document.iterator();
            
            List<String> videoIds = new ArrayList<>();
			
			while(it.hasNext())
			{
				Document doc = it.next();
				videoIds.add(doc.get("_id").toString());
				System.out.println(doc.get("_id").toString());
				
			}
			
			mongoClient.close();
            
            return videoIds;
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
	
	public StreamingOutput downloadVideo(ObjectId videoId)
	{
		MongoClient mongoClient = getDBConnectionService().getDBConnection();

        MongoDatabase database = mongoClient.getDatabase(VideoConstant.DB);
        
		GridFSBucket gridFSBucket = GridFSBuckets.create(database, VideoConstant.COLLECTION); 
		
        
        StreamingOutput streamingOutputVideo = output -> {
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(videoId)) {
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            bytesToWriteTo = downloadStream.readAllBytes();
            
            output.write(bytesToWriteTo);
            
            
        

        }
        };
        //mongoClient.close();
        
        return streamingOutputVideo;
	}
	
	public Document updatePost(Post post, String postId) {
		Document updateDocument;
		try {
			Document userLoggedIn = getUserLoginService().searchSession(post.getUserId());
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			MongoCollection<Document> postCollection = mongoClient
					.getDatabase(PostConstant.DB)
					.getCollection(PostConstant.COLLECTION);
			
			// Check post exists
			Document postDocument = postCollection.find(eq(PostConstant._ID, new ObjectId(postId))).first();
			if(postDocument == null)
				throw new ClientSideException("Post with given id does not exist");
			
			// update the post
			updateDocument = updatePostHelper(post, postDocument, postCollection);
			getDBConnectionService().closeDBConnection();
			System.out.println("Document update complete");
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return updateDocument;
	}
	
	public String deletePhoto(String postId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			MongoCollection<Document> photoCollection = mongoClient
													.getDatabase(PhotoConstant.DB)
													.getCollection(PhotoConstant.COLLECTION);
			
			// Check photo exists
			FindIterable<Document> photoDocument = photoCollection.find(eq(PhotoConstant.POST_ID, postId));
			if(photoDocument == null)
				throw new ClientSideException("Post with given id does not exist");

			 //Deleting the photo
		     DeleteResult photoDeleteResponse = photoCollection.deleteMany(eq(PhotoConstant.POST_ID, postId));
		     System.out.println("Deletion result" + photoDeleteResponse);
		     
		     getDBConnectionService().closeDBConnection();
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return "Photos deleted successfully for postId:" + postId;
	}
	
	public String deleteVideo(String postId) {
		try {
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			MongoCollection<Document> videoCollection = mongoClient
					.getDatabase(VideoConstant.DB)
					.getCollection(VideoConstant.COLLECTION);
			MongoCollection<Document> videoFilesCollection = mongoClient
					.getDatabase(VideoConstant.DB)
					.getCollection("videos.files");
			MongoCollection<Document> videoChunksCollection = mongoClient
					.getDatabase(VideoConstant.DB)
					.getCollection("videos.chunks");
			
			// Check video exists
			FindIterable<Document> videoDocument = videoCollection.find(eq(VideoConstant.POST_ID, postId));
			if(videoDocument == null)
				throw new ClientSideException("Post with given id does not exist");
		     
			 //Deleting the video
			Iterator it = videoDocument.iterator();
			
			while(it.hasNext())
			{
				Document videoDoc = (Document) it.next();
		     DeleteResult videoDeleteResponse1 = videoFilesCollection.deleteOne(eq(VideoConstant._ID, new ObjectId(videoDoc.getString("videoIdGridFS"))));
		     DeleteResult videoDeleteResponse = videoChunksCollection.deleteMany(eq("files_id", new ObjectId(videoDoc.getString("videoIdGridFS"))));
		     System.out.println("Deletion result" + videoDeleteResponse1 + " : " + videoDeleteResponse);
		     
			}
			
			DeleteResult videoDeleteResponse2 = videoCollection.deleteMany(eq(VideoConstant.POST_ID, postId));
			
			System.out.println("Deletion result" + videoDeleteResponse2);
		     
		     getDBConnectionService().closeDBConnection();
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return "Videos deleted successfully for post:" + postId;
	}

	public String deletePost(String postId, String userId) {
		try {
			Document userLoggedIn = getUserLoginService().searchSession(userId);
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			MongoCollection<Document> photoCollection = mongoClient
													.getDatabase(PhotoConstant.DB)
													.getCollection(PhotoConstant.COLLECTION);
			MongoCollection<Document> videoCollection = mongoClient
					.getDatabase(VideoConstant.DB)
					.getCollection(VideoConstant.COLLECTION);
			MongoCollection<Document> postCollection = mongoClient
					.getDatabase(PostConstant.DB)
					.getCollection(PostConstant.COLLECTION);
			
			// Check post exists
			Document postDocument = postCollection.find(eq(PostConstant._ID, new ObjectId(postId))).first();
			if(postDocument == null)
				throw new ClientSideException("Post with given id does not exist");

			 //Deleting the photos
		     DeleteResult photoDeleteResponse = photoCollection.deleteMany(eq(PhotoConstant.POST_ID, new ObjectId(postId)));
		     System.out.println("Deletion result" + photoDeleteResponse);
		     
			 //Deleting the videos
		     DeleteResult videoDeleteResponse = videoCollection.deleteOne(eq(VideoConstant.POST_ID, new ObjectId(postId)));
		     System.out.println("Deletion result" + videoDeleteResponse);
		     
			 //Deleting the post
		     DeleteResult postDeleteResponse = postCollection.deleteOne(eq(PostConstant._ID, new ObjectId(postId)));
		     System.out.println("Deletion result" + postDeleteResponse);
		     
		     getDBConnectionService().closeDBConnection();
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return "Post deleted successfully:" + postId;
	}
	
	private Document updatePostHelper(Post post, Document searchedPost, MongoCollection<Document> collection)
	{
		Document query = new Document();
		query.append(PostConstant._ID, searchedPost.getObjectId(PostConstant._ID));
		
		Document setData = new Document();
        //update post
		if(!post.getTitle().equals(searchedPost.get(PostConstant.TITLE)))
			setData.append(PostConstant.TITLE, post.getTitle());
		if(!post.getDescription().equals(searchedPost.get(PostConstant.DESCRIPTION)))
			setData.append(PostConstant.DESCRIPTION, post.getDescription());
		if(!post.getLocation().equals(searchedPost.get(PostConstant.LOCATION)))
			setData.append(PostConstant.LOCATION, post.getLocation());
		if(!(post.getCrimeType().toString()).equals(searchedPost.get(PostConstant.CRIME_TYPE).toString()))
			setData.append(PostConstant.CRIME_TYPE, post.getCrimeType().toString());
		if(post.getLikesCount() != searchedPost.getInteger(PostConstant.LIKES_COUNT))
			setData.append(PostConstant.LIKES_COUNT, post.getLikesCount());
		if(post.getIsFlagged() != searchedPost.getBoolean(PostConstant.IS_FLAGGED))
			setData.append(PostConstant.IS_FLAGGED, post.getIsFlagged());
		setData.append(PostConstant.TIME_UPDATED, new Date());
		
		Document updateDocument = new Document();
		updateDocument.append("$set", setData);
        UpdateResult postUpdateResponse = collection.updateOne(query, updateDocument);
        
        System.out.println("Post updation response" + postUpdateResponse);
        return updateDocument;
	}

	public DBConnectionService getDBConnectionService()
	{
		if(dbConnectionService == null)
			dbConnectionService = new DBConnectionService();
		return dbConnectionService;
	}
	
	public UserLoginService getUserLoginService()
	{
		if(userLoginService == null)
			userLoginService = new UserLoginService();
		return userLoginService;
	}
}
