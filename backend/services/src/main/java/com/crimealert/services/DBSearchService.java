package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.crimealert.constants.UserConstant;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBSearchService {
	public Document searchPhoneNumber (String phoneNumber, MongoClient mongoClient) {
		try {
			MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
			//Query phone number from the collection
            Document phoneNumberDocument = database.getCollection(UserConstant.COLLECTION).
            			find(eq(UserConstant.PHONE_NUMBER, phoneNumber)).first();
            return phoneNumberDocument;
		} catch(Exception ex) {
    		System.out.println("Response failed:" + ex);
    		throw ex;
    	}
	}
	
	public Document searchEmail (String email, MongoClient mongoClient) {
		try {
			MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
			//Query phone number from the collection
            Document emailDocument = database.getCollection(UserConstant.COLLECTION).
            			find(eq(UserConstant.EMAIL, email)).first();
            return emailDocument;
		} catch(Exception ex) {
    		System.out.println("Response failed:" + ex);
    		throw ex;
    	}
	}
}
