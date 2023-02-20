package com.crimealert.controllers;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.bson.Document;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.models.User;
import com.crimealert.models.UserLogin;
import com.crimealert.services.DBConnectionService;
import com.crimealert.services.DBSearchService;
import com.crimealert.services.UserLoginService;
import com.crimealert.services.UserService;
import com.crimealert.utils.ValidatorUtil;
import com.mongodb.client.MongoClient;

@Path("users")
@Singleton
public class UserController {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@Singleton
	private UserService userService;
	@Singleton
	private UserLoginService userLoginService;
	
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response userRegistration(User user) {
    	try
    	{
    		
    		String validateResponse = ValidatorUtil.validateForm(user); // User side validations
    		
    		if (validateResponse.isEmpty())
    		{	
	    		String userResponse= userService.createUserRegistration(user);
	    		
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
    
    @PUT
    @Path("updateProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response userProfileUpdate(User user) 
    {
    	try {
    	
    	String response = userService.updateUserProfile(user);
    	
    	return Response.ok(response).build();
    	} 
    	catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	}
    	catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }
    
    @DELETE
    @Path("deleteProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response userProfileDelete(String email)
    {
    	try {
    	String response = userService.deleteProfile(email);
    	
    	return Response.ok(response).build();
    	} 
    	catch (ClientSideException ex) {
    		System.out.println("Validation Error:" + ex);
    		return Response.status(400).entity(ex.getMessage()).build();
    	}
    	catch (Exception ex) {
    		System.out.println("Response failed:" + ex);
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
    }
}
