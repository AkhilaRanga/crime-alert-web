package com.crimealert.Validations;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.services.DBSearchService;
import com.mongodb.client.MongoClient;

public class UserValidation {
	
	public Document emailExists(String email,  DBSearchService dbSearchService, MongoClient mongoClient, boolean shouldExist) throws ClientSideException
	{
		Document userDocument = dbSearchService.searchEmail(email, mongoClient);
		
		if(shouldExist && userDocument == null)
			throw new ClientSideException("User with email does not exist");
		
		if(!shouldExist && userDocument != null)
			throw new ClientSideException("User with email exists");
		
		return userDocument;
	}

	public void phoneExists(String phone,  DBSearchService dbSearchService, MongoClient mongoClient, boolean shouldExist) throws ClientSideException
	{
		Document userDocument = dbSearchService.searchPhoneNumber(phone, mongoClient);
		
		if(shouldExist && userDocument == null)
			throw new ClientSideException("User with phone number does not exist");
		
		if(!shouldExist && userDocument != null)
			throw new ClientSideException("User with phone number exists");
	}
}
