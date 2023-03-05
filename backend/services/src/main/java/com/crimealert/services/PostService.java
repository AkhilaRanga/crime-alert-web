package com.crimealert.services;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.PostConstant;
import com.crimealert.models.Post;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

public class PostService {
	private DBConnectionService dbConnectionService;
	
	public Document createPost(Post post) {
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
	    return new Document(); // return post id
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
