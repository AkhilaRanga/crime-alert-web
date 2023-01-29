package com.crimealert.services;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import com.crimealert.models.EncodedPassword;
import com.crimealert.models.User;
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
	
	public String createUserRegistration(User user, MongoClient mongoClient)
	{  
        try {
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
        	//Preparing a document
            Document document = new Document();
            document.append(UserConstant.FULL_NAME, user.getFullName());
            document.append(UserConstant.EMAIL, user.getEmail());
            document.append(UserConstant.LOCATION, user.getLocation());
            document.append(UserConstant.PHONE_NUMBER, user.getPhoneNumber());
            EncodedPassword encoded = getPasswordUtil().generateUserPassword(user.getPassword());
            document.append(UserConstant.PASSWORD, encoded.getEncodedPassword());
            document.append(UserConstant.SALT, encoded.getSalt());
            document.append(UserConstant.ENABLE_NOTIFICATIONS, user.getEnableNotifications());
            //Inserting the document into the collection
            database.getCollection(UserConstant.COLLECTION).insertOne(document);
            System.out.println("Document inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
    return "Registered Successfully for " + user.getFullName();
	}
	
	public String updateUserProfile(User user)
	{
		try
		{
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			UserValidation userValidate = new UserValidation();
			Document searchedDoc = userValidate.isUser(user, getDBSearchService(), mongoClient);
			
			if(searchedDoc == null)
				return "User Profile does not exist";
			
	        MongoCollection<Document> collection = mongoClient
	        		.getDatabase(UserConstant.DB)
	        		.getCollection(UserConstant.COLLECTION);
	        
	        Document query = new Document();
	        query.append("email",user.getEmail());
	        Document setData = new Document();

            //update profile
			if(!user.getFullName().equals(searchedDoc.get("fullName")))
				setData.append("FullName", user.getFullName());
				
			if(!user.getPhoneNumber().equals(searchedDoc.get("phoneNumber")))
				setData.append("PhoneNumber", user.getPhoneNumber());
					
			if(!user.getPassword().equals(searchedDoc.get("password")))
				//need to write logic to update
			
			if(!user.getEnableNotifications().equals(searchedDoc.get("enableNotifications")))
				setData.append("EnableNotifications", user.getEnableNotifications());
							
			if(!user.getLocation().equals(searchedDoc.get("location")))
				setData.append("Location", user.getLocation());
			
			Document update = new Document();
	        update.append("$set", setData);
	        //To update single Document  
	        UpdateResult updRes = collection.updateOne(query, update);
	        
	        System.out.println("Updated Result"+ updRes);
				
		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
		
		 return "Successfully Updated for " + user.getFullName();
	}
	
	public String deleteProfile(String email)
	{
		try
		{
		 MongoCollection<Document> collection = getDBConnectionService()
				.getDBConnection()
        		.getDatabase(UserConstant.DB)
        		.getCollection(UserConstant.COLLECTION);
		
		 Bson query = eq("email", email.trim());
		 
		 //Deleting a document
	     DeleteResult dbResponse = collection.deleteOne(query);
	     
	     System.out.println("Deleted Result"+ dbResponse);
	     
	     //later need to delete from other tables
		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
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
	

}
