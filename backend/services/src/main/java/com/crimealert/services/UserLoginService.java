package com.crimealert.services;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.constants.PhotoConstant;
import com.crimealert.constants.SessionConstant;
import com.crimealert.constants.UserConstant;
import com.crimealert.models.UserLogin;
import com.crimealert.utils.PasswordUtils;
import com.crimealert.utils.SessionTokenUtils;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.eq;

import java.util.Date;

public class UserLoginService {
	
	private PasswordUtils passwordUtil;
	private DBConnectionService dbConnectionService;
	
	public String validateUserLogin(UserLogin userLogin, MongoClient mongoClient)
	{
        try {
            //Get user data from the collection
    		DBSearchService dbSearchService = new DBSearchService();
            Document userDocument = dbSearchService.searchEmail(userLogin.getEmail(), mongoClient);
            if (userDocument == null) {
                System.out.println("User email or password invalid");
                return "User login failed. User email id or password incorrect.";
            }
            String encodedPassword = userDocument.get(UserConstant.PASSWORD).toString();
            String salt = userDocument.get(UserConstant.SALT).toString();
            boolean isValidPassword = getPasswordUtil().verifyUserPassword(userLogin.getPassword(), encodedPassword, salt);
            if (isValidPassword) {
            	setUserSessionToken(userDocument.get(UserConstant._ID).toString());
                System.out.println("User validated");
                Boolean verification = userDocument.getBoolean(UserConstant.VERIFICATION);
                return "User login successful.Verification:" + verification;
            }
            else {
                System.out.println("User email or password invalid");
                return "User login failed. User email id or password incorrect.";
            }
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
	}
	
	public void setUserSessionToken(String userId) {
    	MongoClient mongoClient = getDBConnectionService().getDBConnection();
        MongoCollection<Document> sessionsCollection = mongoClient
				.getDatabase(SessionConstant.DB)
				.getCollection(SessionConstant.COLLECTION);
        
		Document sessionDocument = sessionsCollection.find(eq(SessionConstant.USER_ID, userId)).first();
		if(sessionDocument != null) {
			System.out.println("Session already exists");
			return;
		}
        
        // Generate session token
		String sessionToken = SessionTokenUtils.generateToken(userId);
		
    	//Preparing session document
		Document createDocument = new Document();
		createDocument.append(SessionConstant.USER_ID, userId);
		createDocument.append(SessionConstant.SESSION_TOKEN, sessionToken);
	    createDocument.append(SessionConstant.TIME_CREATED, new Date());
	    
        //Inserting comment document
        System.out.println("Inserting session");
        sessionsCollection.insertOne(createDocument);
        getDBConnectionService().closeDBConnection();
        System.out.println("Session inserted successfully");
	}
	
	public String userLogout(String userId)
	{
        try {
            //Get session data from the collection
        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            MongoCollection<Document> sessionsCollection = mongoClient
    				.getDatabase(SessionConstant.DB)
    				.getCollection(SessionConstant.COLLECTION);
			Document sessionDocument = sessionsCollection.find(eq(SessionConstant.USER_ID, userId)).first();
			if(sessionDocument == null)
				throw new ClientSideException("Session does not exist");

			//Deleting the session
			DeleteResult sessionDeleteResponse = sessionsCollection.deleteOne(eq(SessionConstant.USER_ID, userId));
			System.out.println("Deletion result" + sessionDeleteResponse);
			 
			getDBConnectionService().closeDBConnection();
	    } catch (MongoException me) {
	        System.err.println("An error occurred while attempting to run a command: " + me);
	        throw me;
	    }
	    catch (ClientSideException ce) {
	        System.err.println("An error occurred while attempting to run a command: " + ce);
	        throw ce;
	    }
	    return "Session deleted successfully for:" + userId;
	}
	
	public Document searchSession(String userId)
	{
		MongoClient mongoClient = getDBConnectionService().getDBConnection();
        MongoCollection<Document> sessionsCollection = mongoClient
				.getDatabase(SessionConstant.DB)
				.getCollection(SessionConstant.COLLECTION);
        
		Document sessionDocument = sessionsCollection.find(eq(SessionConstant.USER_ID, userId)).first();
		
		getDBConnectionService().closeDBConnection();
		
		return sessionDocument;
	}
	public PasswordUtils getPasswordUtil()
    {
		if(passwordUtil == null)
			return new PasswordUtils();
		return passwordUtil;	
    }
	
	public DBConnectionService getDBConnectionService()
	{
		if(dbConnectionService == null)
			dbConnectionService = new DBConnectionService();
		return dbConnectionService;
	}
}