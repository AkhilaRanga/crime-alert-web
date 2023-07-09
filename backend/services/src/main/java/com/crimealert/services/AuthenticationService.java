package com.crimealert.services;

import org.bson.Document;

import com.crimealert.models.Otp;
import com.crimealert.models.User;
import com.crimealert.utils.EmailSender;
import com.crimealert.utils.SessionTokenUtils;
import com.mongodb.client.MongoClient;
import com.crimealert.Exceptions.ClientSideException;

public class AuthenticationService {
	
	private UserService userService;
	private DBConnectionService dbConnectionService;
	private DBSearchService dbSearchService;
	
	
	public  void requestOtp(String emailId)
	{
		MongoClient mongoClient = getDBConnectionService().getDBConnection();
        
        Document userRegistered = getDBSearchService().searchEmail(emailId, mongoClient);
        
        System.out.println("User Registered : " + userRegistered);
        
        if(userRegistered != null)
        {
		
        	String token = SessionTokenUtils.generateToken(emailId);
		
        	String provider = getProvider(emailId);
		
        	if(EmailSender.sendOTP(emailId, token, provider))
        	{
        		Otp otp = new Otp();
        		otp.setEmail(emailId);
        		otp.setOneTimeToken(token);
			
        		getUserService().insertOtp(otp);
        	}else
        	{
        		throw new ClientSideException("Failed to send otp");
        	}
        }
        else
        {
        	throw new ClientSideException("User is not registered");
        }
		
	}
	
	public void verifyOtp(Otp otp)
	{
		
		Document foundOtp = getUserService().searchOtp(otp);
		
		System.out.println("Found otp:" + foundOtp);
		
		if(foundOtp == null)
		{
			throw new ClientSideException("Failed to verify otp");
		}
		
		getUserService().deleteOtp(otp);
		
		User user = new User();
		user.setEmail(otp.getEmail());
		user.setVerififcation(true);
		getUserService().updateUserProfile(user);
		
	}
	
	private String getProvider(String emailId)
	{
		if(emailId.contains("gmail"))
			return "gmail";
		else if(emailId.contains("yahoo"))
			return "Yahoo";
		
		return "";		
	}
	private UserService getUserService()
	{
		if(userService == null)
			userService = new UserService();
		return userService;
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
