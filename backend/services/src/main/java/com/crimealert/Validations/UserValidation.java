package com.crimealert.Validations;

import org.bson.Document;

import com.crimealert.models.User;
import com.crimealert.services.DBSearchService;
import com.mongodb.client.MongoClient;

public class UserValidation {
	
	public Document isUser(User user, DBSearchService dbSearchService, MongoClient mongoClient)
	{
		return dbSearchService.searchEmail(user.getEmail(), mongoClient);
	
	}

}
