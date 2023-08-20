package com.crimealert.services;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import com.crimealert.models.EncodedPassword;
import com.crimealert.models.Otp;
import com.crimealert.models.User;
import com.crimealert.Exceptions.ClientSideException;
import com.crimealert.Validations.UserValidation;
import com.crimealert.constants.AuthenticationConstant;
import com.crimealert.constants.UserConstant;
import com.crimealert.utils.PasswordUtils;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserService {
	
	private PasswordUtils passwordUtil;
	private DBConnectionService dbConnectionService;
	private DBSearchService dbSearchService;
	private UserLoginService userLoginService;
	private UserValidation userValidation;
	
	public String createUserRegistration(User user)
	{  
        try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
             
            getUserValidation().emailExists(user.getEmail(), getDBSearchService(), mongoClient, false);
            
            userValidation.phoneExists(user.getPhoneNumber(), getDBSearchService(), mongoClient, false);
            
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
        	//Preparing a document
            Document document = registerHelper(user);
            //Inserting the document into the collection
            database.getCollection(UserConstant.COLLECTION).insertOne(document);
            
            getDBConnectionService().closeDBConnection();
            
            System.out.println("Document inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }
        return "Registered Successfully for " + user.getFullName();
	}
	
	public Document getUserProfile(String email)
	{
		try
		{
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			MongoCollection<Document> userCollection = mongoClient
	        		.getDatabase(UserConstant.DB)
	        		.getCollection(UserConstant.COLLECTION);
			
			Bson projectionFields = Projections.fields(
                    Projections.exclude(UserConstant.PASSWORD, UserConstant.SALT));
            Document userDocument = userCollection.
            			find(eq(UserConstant.EMAIL, email)).projection(projectionFields).first();
            
			System.out.println("Searched Document:"+ userDocument);
            return userDocument;
		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
		catch (ClientSideException ce) {
            System.err.println("Validation Error : " + ce);
            throw ce;
        }
	}
	
	public String updateUserProfile(User user)
	{
		try
		{
				
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
			
			Document searchedDoc = getUserValidation().emailExists(user.getEmail(), getDBSearchService(), mongoClient, true);
			
			System.out.println("Searched Document:"+ searchedDoc);
			
	        
			MongoCollection<Document> collection = mongoClient
	        		.getDatabase(UserConstant.DB)
	        		.getCollection(UserConstant.COLLECTION);
	        
	        updateUserHelper(user, searchedDoc, collection);

	        getDBConnectionService().closeDBConnection();

		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
		catch (ClientSideException ce) {
            System.err.println("Validation Error : " + ce);
            throw ce;
        }
		
		 return "Successfully Updated for " + user.getEmail();
	}
	
	public String deleteProfile(String email)
	{
		try
		{
			Document userLoggedIn = getUserLoginService().searchSession(email);
			
			if(userLoggedIn == null)
				throw new ClientSideException("User is not logged In");
			
			MongoClient mongoClient = getDBConnectionService().getDBConnection();
					
			getUserValidation().emailExists(email, getDBSearchService(), mongoClient, true);
			
			MongoCollection<Document> collection = mongoClient
													.getDatabase(UserConstant.DB)
													.getCollection(UserConstant.COLLECTION);
		 
		 //Deleting a document
	     DeleteResult dbResponse = collection.deleteOne(eq("email", email.trim()));
	     
	     System.out.println("Deleted Result"+ dbResponse);
	     
	     getDBConnectionService().closeDBConnection();
	     
	     //later need to delete from other tables
		}catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }catch (ClientSideException ce) {
            System.err.println("Validation Error : " + ce);
            throw ce;
        }
	      
	     return "Deleted Document Successfully"; 
		
	}
	
	public Document insertOtp(Otp otp)
	{
		Document document = new Document();
		try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
            
        	//Preparing a document
            
            document.append(UserConstant.EMAIL, otp.getEmail());
            document.append(AuthenticationConstant.TOKEN, otp.getOneTimeToken());
            
            //Inserting the document into the collection
            database.getCollection(AuthenticationConstant.OTP_COLLECTION).insertOne(document);
            
            getDBConnectionService().closeDBConnection();
            
            System.out.println("Document inserted successfully");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }
		
		return document;
		
	}
	
	public long deleteOtp(Otp otp)
	{
		try {

        	MongoClient mongoClient = getDBConnectionService().getDBConnection();
            
            MongoDatabase database = mongoClient.getDatabase(UserConstant.DB);
            
            //Inserting the document into the collection
            Bson filter = Filters.and(Filters.eq(UserConstant.EMAIL, otp.getEmail()), Filters.eq(AuthenticationConstant.TOKEN, otp.getOneTimeToken()));
            DeleteResult dbResponse = database.getCollection(AuthenticationConstant.OTP_COLLECTION).deleteOne(filter);
            
            getDBConnectionService().closeDBConnection();
            
            System.out.println("Document deleted successfully");
            
            return dbResponse.getDeletedCount();
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
        catch (ClientSideException ce) {
            System.err.println("An error occurred while attempting to run a command: " + ce);
            throw ce;
        }

	}
	
	public Document searchOtp(Otp otp)
	{
		MongoClient mongoClient = getDBConnectionService().getDBConnection();
		
		return getDBSearchService().searchOtp(otp.getEmail(), otp.getOneTimeToken(), mongoClient);
		
	}
	
	public PasswordUtils getPasswordUtil()
    {
		if(passwordUtil == null)
			return new PasswordUtils();
		return passwordUtil;
    	
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
	
	public UserValidation getUserValidation()
	{
		if(userValidation == null)
			userValidation = new UserValidation();
		return userValidation;
	}
	
	public UserLoginService getUserLoginService()
	{
		if(userLoginService == null)
			userLoginService = new UserLoginService();
		return userLoginService;
	}
	private void updateUserHelper(User user, Document searchedUser, MongoCollection<Document> collection)
	{
		Document query = new Document();
        query.append("email",user.getEmail());
        Document setData = new Document();

        //update profile
		if(user.getFullName() != null && (!user.getFullName().equals(searchedUser.get("fullName"))))
			setData.append("fullName", searchedUser.get("fullName"));
		
		System.out.println("Searched Full name:" +  searchedUser.get("fullName"));
			
		if( user.getPhoneNumber() != null && !user.getPhoneNumber().equals(searchedUser.get("phoneNumber")))
			setData.append("phoneNumber", user.getPhoneNumber());
				
		if( user.getPassword() != null && !getPasswordUtil().verifyUserPassword(user.getPassword(),searchedUser.get("password").toString(),searchedUser.get("salt").toString()))
		{
			EncodedPassword encoded = getPasswordUtil().generateUserPassword(user.getPassword());
			setData.append("password", encoded.getEncodedPassword());
			setData.append("salt", encoded.getSalt());
		}
			//need to write logic to update
		if( user.getEnableNotifications() != null && !user.getEnableNotifications().equals(searchedUser.get("enableNotifications")))
			setData.append("enableNotifications", user.getEnableNotifications());
		
		if( user.getVerification() != null && !user.getVerification().equals(searchedUser.get("verification")))
			setData.append("verification", user.getVerification());
						
		if(user.getLocation() != null && !user.getLocation().equals(searchedUser.get("location")))
			setData.append("location", user.getLocation());
		
		if(!setData.isEmpty())
		{
		Document update = new Document();
		
        update.append("$set", setData);
        //To update single Document  
        UpdateResult updRes = collection.updateOne(query, update);
        
        System.out.println("Updated Result"+ updRes);
		}
		
		
	}
	
	private Document registerHelper(User user)
	{
		Document document = new Document();
        document.append(UserConstant.FULL_NAME, user.getFullName());
        document.append(UserConstant.EMAIL, user.getEmail());
        document.append(UserConstant.LOCATION, user.getLocation());
        document.append(UserConstant.PHONE_NUMBER, user.getPhoneNumber());
        EncodedPassword encoded = getPasswordUtil().generateUserPassword(user.getPassword());
        document.append(UserConstant.PASSWORD, encoded.getEncodedPassword());
        document.append(UserConstant.SALT, encoded.getSalt());
        document.append(UserConstant.ENABLE_NOTIFICATIONS, false);	
        document.append(UserConstant.VERIFICATION, false);	
        
        return document;
	}
	

}
