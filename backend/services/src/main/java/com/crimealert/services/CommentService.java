package com.crimealert.services;

import java.util.List;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.CommentConstant;
import com.crimealert.models.Comment;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CommentService {
	
	private DBConnectionService dbConnectionService;
	
	public Document createComment(Comment comment) {
		Document document = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(CommentConstant.DB);
            
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
	
	public Document updateComment(Comment comment) {
		Document document = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(CommentConstant.DB);
            
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
	
	public List<Document> listComments(String postId) {
		List<Document> comments = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(CommentConstant.DB);
            
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return comments; // return post id
	}
	
	public Document getComment(String commentId) {
		Document comment = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(CommentConstant.DB);
            
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return comment; // return post id
	}
	
	public Document deleteComment(String commentId) {
		Document comment = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

            MongoDatabase database = mongoClient.getDatabase(CommentConstant.DB);
            
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return comment; // return post id
	}
	
	public DBConnectionService getDBConnectionService()
	{
		if(dbConnectionService == null)
			dbConnectionService = new DBConnectionService();
		return dbConnectionService;
	}

}
