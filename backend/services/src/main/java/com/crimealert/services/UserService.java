package com.crimealert.services;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import com.crimealert.models.EncodedPassword;
import com.crimealert.models.User;
import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.Validations.UserValidation;
import com.crimealert.constants.UserConstant;
import com.crimealert.utils.PasswordUtils;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserService {
	
	private PasswordUtils passwordUtil;
	private DBConnectionService dbConnectionService;
	private DBSearchService dbSearchService;
	private UserValidation userValidation;
	
	public String createUserRegistration(User user)
	{  
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
             
            Document userDoc = userValidation.emailExists(user.getEmail(), getDBSearchService(), mongoClient);
            
            if(userDoc != null)
            	throw new ClientSideException("User exists");
            
            userValidation.phoneExists(user.getPhoneNumber(), getDBSearchService(), mongoClient);
            
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
        	//Preparing a document
            Document document = registerHelper(user);
            //Inserting the document into the collection
            database.getCollection(UserConstant.COLLECTION).insertOne(document);
            
            getDBConnectionService().closeDBConnection();
            
            System.out.println("Document inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }
        return "Registered Successfully for " + user.getFullName();
	}
	
	public String updateUserProfile(User user)
	{
		try
		{
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			
			Document searchedDoc = userValidation.emailExists(user.getEmail(), getDBSearchService(), mongoClient);
			
	        
			MongoCollection<Document> collection = mongoClient
	        		.getDatabase(UserConstant.DB)
	        		.getCollection(UserConstant.COLLECTION);
	        
	        updateUserHelper(user, searchedDoc, collection);

	        getDBConnectionService().closeDBConnection();

		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
		catch (ClientSideException ce) {
            System.err.println("Validation Error : " + ce);
            throw ce;
        }
		
		 return "Successfully Updated for " + user.getFullName();
	}
	
	public String deleteProfile(String email)
	{
		try
		{
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
					
			userValidation.emailExists(email, getDBSearchService(), mongoClient);
			
			MongoCollection<Document> collection = mongoClient
													.getDatabase(UserConstant.DB)
													.getCollection(UserConstant.COLLECTION);
		 
		 //Deleting a document
	     DeleteResult dbResponse = collection.deleteOne(eq("email", email.trim()));
	     
	     System.out.println("Deleted Result"+ dbResponse);
	     
	     getDBConnectionService().closeDBConnection();
	     
	     //later need to delete from other tables
		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }catch (ClientSideException ce) {
            System.err.println("Validation Error : " + ce);
            throw ce;
        }
	      
	     return "Deleted Document Successfully"; 
		
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
	
	public DBSearchService getDBSearchService()
	{
		if(dbSearchService == null)
			dbSearchService = new DBSearchService();
		return dbSearchService;
	}
	
	public UserValidation getUserValidation()
	{
		if(userValidation == null)
			userValidation = new UserValidation();
		return userValidation;
	}
	
	private void updateUserHelper(User user, Document searchedUser, MongoCollection<Document> collection)
	{
		Document query = new Document();
        query.append("email",user.getEmail());
        Document setData = new Document();

        //update profile
		if(!user.getFullName().equals(searchedUser.get("fullName")))
			setData.append("FullName", user.getFullName());
			
		if(!user.getPhoneNumber().equals(searchedUser.get("phoneNumber")))
			setData.append("PhoneNumber", user.getPhoneNumber());
				
		if(!user.getPassword().equals(searchedUser.get("password")))
			//need to write logic to update
		
		if(!user.getEnableNotifications().equals(searchedUser.get("enableNotifications")))
			setData.append("EnableNotifications", user.getEnableNotifications());
						
		if(!user.getLocation().equals(searchedUser.get("location")))
			setData.append("Location", user.getLocation());
		
		if(!setData.isEmpty())
		{
		Document update = new Document();
		
        update.append("$set", setData);
        //To update single Document  
        UpdateResult updRes = collection.updateOne(query, update);
        
        System.out.println("Updated Result"+ updRes);
		}
		
		
	}
	
	private Document registerHelper(User user)
	{
		Document document = new Document();
        document.append(UserConstant.FULL_NAME, user.getFullName());
        document.append(UserConstant.EMAIL, user.getEmail());
        document.append(UserConstant.LOCATION, user.getLocation());
        document.append(UserConstant.PHONE_NUMBER, user.getPhoneNumber());
        EncodedPassword encoded = getPasswordUtil().generateUserPassword(user.getPassword());
        document.append(UserConstant.PASSWORD, encoded.getEncodedPassword());
        document.append(UserConstant.SALT, encoded.getSalt());
        document.append(UserConstant.ENABLE_NOTIFICATIONS, user.getEnableNotifications());	
        
        return document;
	}
	

}
