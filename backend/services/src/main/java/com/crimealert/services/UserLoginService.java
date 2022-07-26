package com.crimealert.services;

import org.bson.Document;

import com.crimealert.constants.UserConstant;
import com.crimealert.models.UserLogin;
import com.crimealert.utils.PasswordUtils;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class UserLoginService {
	
	private PasswordUtils passwordUtil;
	
	public String validateUserLogin(UserLogin userLogin)
	{
		String uri = "mongodb+srv://admin:crime-web@crime-alert-app-cluster.iqtzt6l.mongodb.net/?retryWrites=true&w=majority";
		
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
            
            try {
                //Get user data from the collection
                Document userDocument = database.getCollection(UserConstant.COLLECTION).
                			find(eq(UserConstant.EMAIL, userLogin.getEmail())).first();
                if (userDocument == null) {
                	mongoClient.close();
                    System.out.println("User email or password invalid");
                    return "User login failed. User email id or password incorrect.";
                }
                String encodedPassword = userDocument.get(UserConstant.PASSWORD).toString();
                String salt = userDocument.get(UserConstant.SALT).toString();
                boolean isValidPassword = getPasswordUtil().verifyUserPassword(userLogin.getPassword(), encodedPassword, salt);
                mongoClient.close();
                if (isValidPassword) {
                    System.out.println("User validated");
                    return "User login successful";
                }
                else {
                    System.out.println("User email or password invalid");
                    return "User login failed. User email id or password incorrect.";
                }
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
                throw me;
            }
        }
	}
	
	public PasswordUtils getPasswordUtil()
    {
		if(passwordUtil == null)
			return new PasswordUtils();
		return passwordUtil;
    	
    }


}
