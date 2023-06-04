package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.CommentConstant;
import com.crimealert.constants.UserConstant;
import com.crimealert.models.Comment;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CommentService {
	
	private DBConnectionService dbConnectionService;
	
	private Document prepareCommentInsert(Comment comment)
	{
		Document document = new Document();
	    document.append(CommentConstant.USER_ID, comment.getUserId());
	    document.append(CommentConstant.PARENT_ID, comment.getParentId());
	    document.append(CommentConstant.POST_ID, comment.getPostId());
	    document.append(CommentConstant.COMMENT, comment.getComment());
	    document.append(CommentConstant.IS_FLAGGED, false);
	    document.append(CommentConstant.LIKES_COUNT, 0);
	    document.append(CommentConstant.TIME_CREATED, new Date());	
	    return document;
	}
	
	public Document createComment(Comment comment) {
		Document createDocument;
        try {
        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            MongoCollection<Document> commentsCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
            MongoCollection<Document> usersCollection = mongoClient
					.getDatabase(UserConstant.DB)
					.getCollection(UserConstant.COLLECTION);
			// Check user exists
			Document userDocument = usersCollection.find(eq(UserConstant._ID, new ObjectId(comment.getUserId()))).first();
			if(userDocument == null)
				throw new ClientSideException("User with given id does not exist");
            System.out.println("User validated");
        	//Preparing comment document
			createDocument = prepareCommentInsert(comment);
            //Inserting comment document
            System.out.println("Inserting");
			commentsCollection.insertOne(createDocument);
            comment.setId(createDocument.getObjectId(CommentConstant._ID).toString());
            getDBConnectionService().closeDBConnection();
            System.out.println("Comment inserted successfully");
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return createDocument;
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
		List<Document> comments = new ArrayList<>();
        try {
        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            MongoCollection<Document> commentsCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
            FindIterable<Document> listDocuments =  commentsCollection.find(eq(CommentConstant.POST_ID, postId));
            Iterator listIterator = listDocuments.iterator();
            while (listIterator.hasNext()) {
            	comments.add((Document) listIterator.next());
            }
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return comments;
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
