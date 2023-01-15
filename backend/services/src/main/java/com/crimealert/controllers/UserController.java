package com.crimealert.controllers;

import jakarta.ws.rs.Consumes; 
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.bson.Document;

import com.crimealert.models.User;
import com.crimealert.models.UserLogin;
import com.crimealert.services.DBConnectionService;
import com.crimealert.services.DBSearchService;
import com.crimealert.services.UserLoginService;
import com.crimealert.services.UserService;
import com.crimealert.utils.ValidatorUtil;
import com.mongodb.client.MongoClient;

@Path("users")
public class UserController {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response userRegistration(User user) {
    	UserService userService =  new UserService();

    	try
    	{
    		
    		String validateResponse = ValidatorUtil.validateForm(user);
    		
    		if (validateResponse.isEmpty())
    		{
        		DBConnectionService dbConnectionService = new DBConnectionService();
        		MongoClient dBConnection = dbConnectionService.getDBConnection();
        		// unique phone number and email id
        		DBSearchService dbSearchService = new DBSearchService();
        		Document userDocument = dbSearchService.searchEmail(user.getEmail(), dBConnection);
        		if (userDocument != null) {
        			dbConnectionService.closeDBConnection();
        			System.out.println("Validation Error: Email already exists");
        			return Response.status(400).entity("Email already exists").build();
        		}
        		userDocument = dbSearchService.searchPhoneNumber(user.getPhoneNumber(), dBConnection);
        		if (userDocument != null) {
        			dbConnectionService.closeDBConnection();
        			System.out.println("Validation Error: Phone number already exists");
        			return Response.status(400).entity("Phone number already exists").build();
        		}
        		
	    		String userResponse= userService.createUserRegistration(user, dBConnection);
    			dbConnectionService.closeDBConnection();
	    		System.out.println("Response received:" + userResponse);
	    		return Response.ok(userResponse).build();
    		}
    		else
    		{
    			System.out.println("Validation Error:" + validateResponse);
    			return Response.status(400).entity(validateResponse).build();
    		}
    	}catch(Exception ex)
    	{
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response userLogin(UserLogin userLogin) {
    	UserLoginService userLoginService = new UserLoginService();
    	
    	try {
    		// Validate the received request body
    		String validateResponse = ValidatorUtil.validateForm(userLogin);
    		System.out.println("validateresponse" + validateResponse);
    		
    		if (validateResponse.isEmpty()) {
        		DBConnectionService dbConnectionService = new DBConnectionService();
        		MongoClient dBConnection = dbConnectionService.getDBConnection();
    			String userLoginResponse = userLoginService.validateUserLogin(userLogin, dBConnection);
	    		dbConnectionService.closeDBConnection();
        		System.out.println("Response received:" + userLoginResponse);
        		return Response.ok(userLoginResponse).build();
    		}
    		else {
    			System.out.println("Validation Error:" + validateResponse);
    			return Response.status(400).entity(validateResponse).build();
    		}
    	} catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }
}
