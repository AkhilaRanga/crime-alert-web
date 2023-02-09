package com.crimealert.Validations;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.services.DBSearchService;
import com.mongodb.client.MongoClient;

public class UserValidation {
	
	public Document emailExists(String email,  DBSearchService dbSearchService, MongoClient mongoClient) throws ClientSideException
	{
		Document userDocument = dbSearchService.searchEmail(email, mongoClient);
		
		if(userDocument == null)
			throw new ClientSideException("User does not exist");
		
		return userDocument;
	}

	public void phoneExists(String phone,  DBSearchService dbSearchService, MongoClient mongoClient) throws ClientSideException
	{
		Document userDocument = dbSearchService.searchPhoneNumber(phone, mongoClient);
		
		if(userDocument != null)
			throw new ClientSideException("Phone number exists");
	}
}
