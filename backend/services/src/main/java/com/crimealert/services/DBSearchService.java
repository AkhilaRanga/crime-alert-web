package com.crimealert.services;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.crimealert.constants.UserConstant;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

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
	
	public Document searchOtp (String email, String token, MongoClient mongoClient) {
		try {
			MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
			//Query OTP from the collection
			Bson filter = Filters.and(Filters.eq(UserConstant.EMAIL, email), Filters.eq(UserConstant.TOKEN, token));
            Document otpDocument = database.getCollection(UserConstant.OTP_COLLECTION).
            			find(filter).first();
            return otpDocument;
		} catch(Exception ex) {
    		System.out.println("Response failed:" + ex);
    		throw ex;
    	}
	}
}
