package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.CommentConstant;
import com.crimealert.models.Comment;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

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

        	MongoCollection<Document> commentCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
            
            Document oldComment = commentCollection.find(eq(CommentConstant._ID, new ObjectId(comment.getId()))).first();
            
			if(oldComment == null)
				throw new ClientSideException("Comment with given id does not exist");
			
			// update the comment
			document = updateCommentHelper(comment,oldComment);
			
			Document query = new Document();
			query.append(CommentConstant._ID, comment.getId());
			
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
		Document oldComment = null;
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();

        	MongoCollection<Document> commentCollection = mongoClient
					.getDatabase(CommentConstant.DB)
					.getCollection(CommentConstant.COLLECTION);
        	
        	oldComment = commentCollection.find(eq(CommentConstant._ID, new ObjectId(commentId))).first();
            
			if(oldComment == null)
				throw new ClientSideException("Comment with given id does not exist");
			
			if(oldComment.get("parentId") == null || oldComment.get("parentId").toString().isEmpty())
			{
				FindIterable<Document>childComments = commentCollection.find(eq(CommentConstant.PARENT_ID, new ObjectId(commentId)));
				
				for (Document document : childComments) {
					commentCollection.deleteOne(document);
				}
				
			}
			else
			{
				DeleteResult commentDeleteResponse = commentCollection.deleteOne(eq(CommentConstant._ID, new ObjectId(commentId)));
			     System.out.println("Deletion result" + commentDeleteResponse);
			}
            
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
        //update post
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

}
