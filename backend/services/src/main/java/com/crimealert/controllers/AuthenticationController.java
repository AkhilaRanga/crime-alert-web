package com.crimealert.controllers;

import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.models.Otp;
import com.crimealert.services.AuthenticationService;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
public class AuthenticationController {
	
	private AuthenticationService authenticationService;
	
	
	@POST
	@Path("requestOtp/{emailId}")
    @Produces(MediaType.TEXT_PLAIN)
	public Response requestOpt(
			@NotNull@PathParam("emailId")String emailId
	) {
		try {
			getAuthenticationService().requestOtp(emailId);
			
			return Response.status(200).entity("OTP sent successfully").build();
			
		} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.toString()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	@POST
	@Path("verifyOtp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public Response verifyOpt(
			Otp otp
	) {
		try {
			
			getAuthenticationService().verifyOtp(otp);
			
			return Response.status(200).entity("OTP verified successfully").build();
			
		} catch (ClientSideException ex) {
    		return Response.status(400).entity(ex.toString()).build();
    	} catch (Exception ex) {
    		return Response.status(500).entity(ex.getMessage()).build();
    	}
	}
	
	public AuthenticationService getAuthenticationService()
	{
		if(authenticationService == null)
			authenticationService = new AuthenticationService();
		return authenticationService;
	}

}
