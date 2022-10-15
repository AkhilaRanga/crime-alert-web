package com.crimealert.services;

import org.bson.Document;

import com.crimealert.models.EncodedPassword;
import com.crimealert.models.User;
import com.crimealert.constants.UserConstant;
import com.crimealert.utils.PasswordUtils;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class UserService {
	
	private PasswordUtils passwordUtil;
	
	public String createUserRegistration(User user)
	{
		String uri = "";
		
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
            
            try {
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
                
                mongoClient.close();
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
                throw me;
            }
        }

        return "Registered Successfully for" + user.getFullName();
	}
	
	public PasswordUtils getPasswordUtil()
    {
		if(passwordUtil == null)
			return new PasswordUtils();
		return passwordUtil;
    	
    }

}
