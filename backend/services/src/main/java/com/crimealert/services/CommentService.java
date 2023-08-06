package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.CommentConstant;
import com.crimealert.constants.UserConstant;
import com.crimealert.models.Comment;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommentService {
	
	private DBConnectionService dbConnectionService;
	private UserLoginService userLoginService;
	
	private Document prepareCommentInsert(Comment comment)
	{
		Document document = new Document();
	    document.append(CommentConstant.USER_ID, comment.getUserId());
	    document.append(CommentConstant.PARENT_ID, comment.getParentId());
	    document.append(CommentConstant.POST_ID, comment.getPostId());
	    document.append(CommentConstant.COMMENT, comment.getComment());
	    document.append(CommentConstant.IS_FLAGGED, false);
	    document.append(CommentConstant.LIKES_COUNT, 0);
	    document.append(CommentConstant.IS_DELETED, false);
	    document.append(CommentConstant.TIME_CREATED, new Date());	
	    return document;
	}
	
	public Document createComment(Comment comment) {
		Document createDocument;
        try {
        	Document userLoggedIn = getUserLoginService().searchSession(comment.getUserId());
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");
			
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
	
	public Document updateComment(Comment comment, String commentId) {
		Document document = null;
        try {
        	Document userLoggedIn = getUserLoginService().searchSession(comment.getUserId());
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
        	
        	System.out.println("Retrieved mongoclient");

        	MongoCollection<Document> commentCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
            
            Document oldComment = commentCollection.find(eq(CommentConstant._ID, new ObjectId(commentId))).first();
            
			if(oldComment == null)
				throw new ClientSideException("Comment with given id does not exist");
			
			// update the comment
			document = updateCommentHelper(comment,oldComment);
			
			Document query = new Document();
			query.append(CommentConstant._ID, new ObjectId(commentId));
			
			UpdateResult postUpdateResponse = commentCollection.updateOne(query, document);
			
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
	    return document;
	}
	
	public List<Document> listComments(String postId) {
		List<Document> comments = new ArrayList<>();
        try {
        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            MongoCollection<Document> commentsCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
            Bson filter = Filters.eq(CommentConstant.POST_ID, postId);
            AggregateIterable<Document> results = commentsCollection.aggregate(Arrays.asList(
                    new Document("$match", filter),
                    new Document("$addFields", new Document("string_id", new Document("$toString", "$_id")))
                ));
            for (Document doc : results) {
            	comments.add(doc);
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
	    return comment; 
	}
	
	public Document deleteComment(String commentId, String userId) {
		Document oldComment = null;
        try {
        	Document userLoggedIn = getUserLoginService().searchSession(userId);
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

        	MongoCollection<Document> commentCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
        	
        	oldComment = commentCollection.find(eq(CommentConstant._ID, new ObjectId(commentId))).first();
        	
        	
            
			if(oldComment == null)
				throw new ClientSideException("Comment with given id does not exist");
			
//			if(oldComment.get(CommentConstant.PARENT_ID) == null || oldComment.get(CommentConstant.PARENT_ID).toString().isEmpty())
//			{
				
//				FindIterable<Document>childComments = commentCollection.find(eq(CommentConstant.PARENT_ID, commentId));
				
//				System.out.println("Number of child comments : " +  childComments.first());
//				
//				for (Document document : childComments) {
//					System.out.println("Child doc:" +  document.get(CommentConstant._ID));
//					commentCollection.deleteOne(eq(CommentConstant._ID, document.get(CommentConstant._ID)));
//				}Document setData = new Document();
//			}
		
//			DeleteResult commentDeleteResponse = commentCollection.deleteOne(eq(CommentConstant._ID, new ObjectId(commentId)));

			
	        //update comment
			Document setData = new Document();
			setData.append(CommentConstant.COMMENT, "-");
			setData.append(CommentConstant.IS_DELETED, true);
			setData.append(CommentConstant.TIME_UPDATED, new Date());
			
			Document updateDocument = new Document();
			updateDocument.append("$set", setData);
			
			Document query = new Document();
			query.append(CommentConstant._ID, new ObjectId(commentId));
			
			UpdateResult commentDeleteResponse = commentCollection.updateOne(query, updateDocument);
			System.out.println("Deletion result" + commentDeleteResponse);
			
			getDBConnectionService().closeDBConnection();
            
        } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return oldComment; // returns the deleted document
	}
	
	private Document updateCommentHelper(Comment newComment, Document oldComment)
	{
		
		
		Document setData = new Document();
        //update comment
		if(!newComment.getComment().equals(oldComment.get(CommentConstant.COMMENT)))
			setData.append(CommentConstant.COMMENT, newComment.getComment());
		if(newComment.getLikesCount() != oldComment.getInteger(CommentConstant.LIKES_COUNT))
			setData.append(CommentConstant.LIKES_COUNT, newComment.getLikesCount());
		if(newComment.getIsFlagged() != oldComment.getBoolean(CommentConstant.IS_FLAGGED))
			setData.append(CommentConstant.IS_FLAGGED, newComment.getIsFlagged());
		setData.append(CommentConstant.TIME_UPDATED, new Date());
		
		Document updateDocument = new Document();
		updateDocument.append("$set", setData);

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
