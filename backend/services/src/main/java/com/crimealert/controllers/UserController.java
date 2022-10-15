package com.crimealert.controllers;

import jakarta.ws.rs.Consumes; 
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import com.crimealert.models.User;
import com.crimealert.services.UserService;
import com.crimealert.utils.ValidatorUtil;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response userRegistration(User user) {
    	UserService userService =  new UserService();

    	try
    	{
    		
    		String validateResponse = ValidatorUtil.validateForm(user);
    		
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
}
