package com.crimealert.services;

import java.io.File;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.PostConstant;
import com.crimealert.models.Photo;
import com.crimealert.models.Post;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;


public class PostService {
	
	private DBConnectionService dbConnectionService;
	
	public Post createPosts(Post post)
	{  
		Document document;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            
            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
        	//Preparing a document
            document = preparePostInsert(post);
            //Inserting the document into the collection
            database.getCollection(PostConstant.POST_COLLECTION).insertOne(document);
            
            post.setPostId(document.getObjectId("_id"));
            
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
        return post;
	}
	
	public Photo createPhotos(Photo photo)
	{
		
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            
            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
            
            Document document = new Document();
            
        	//Preparing a document
            document.append(PostConstant.POST_ID, photo.getPostId());
            document.append(PostConstant.TITLE, photo.getTitle());
            document.append(PostConstant.IMAGE, photo.getImage());
            
            //Inserting the document into the collection
            database.getCollection(PostConstant.PHOTO_COLLECTION).insertOne(document);
            
            photo.setPhotoId(document.getObjectId("_id"));
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
	
	private Document preparePostInsert(Post post)
	{
		Document document = new Document();
        document.append(PostConstant.USER_ID, post.getUserId());
        document.append(PostConstant.DESCRIPTION, post.getDescription());
        document.append(PostConstant.LOCATION, post.getLocation());
        document.append(PostConstant.TITLE, post.getTitle());
        document.append(PostConstant.TYPE_OF_CRIME, post.getTypeOfCrime());
        document.append(PostConstant.FLAG_OPTION, post.getFlagOption());
        document.append(PostConstant.LIKES, post.getLikes());
        document.append(PostConstant.TIME_CREATED, post.getTimeCreated());	
        document.append(PostConstant.TIME_UPDATED, post.getTimeUpdated());	
        
        return document;
	}
	
	public Document getPost(Post post)
	{
		try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            
            MongoDatabase database = mongoClient.getDatabase(PostConstant.DB);
            
            BasicDBObject query = new BasicDBObject();
            query.put("_id", post.getPostId());

            Document document =  (Document) database.getCollection(PostConstant.POST_COLLECTION).find(query);
            
            return document;
            
            
		 } catch (MongoException me) {
	            System.err.println("An error occurred while attempting to run a command: " + me);
	            throw me;
	        }
	        catch (ClientSideException ce) {
	            System.err.println("An error occurred while attempting to run a command: " + ce);
	            throw ce;
	        }
		return post;
		
	}
	
	private DBConnectionService getDBConnectionService()
	{
		if(dbConnectionService == null)
			dbConnectionService = new DBConnectionService();
		return dbConnectionService;
	}

}
