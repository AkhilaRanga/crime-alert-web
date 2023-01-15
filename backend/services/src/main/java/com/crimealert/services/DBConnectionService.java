package com.crimealert.services;

import java.util.concurrent.TimeUnit;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DBConnectionService {
	private MongoClient mongoClient;
	
	public void setDBConnection () {
		String uri = "";
		try {
			MongoClient mongoClient = MongoClients.create(
				    MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri))
				    .applyToConnectionPoolSettings(builder ->
				        builder.maxWaitTime(10, TimeUnit.SECONDS)
				        .maxSize(200))
				    .build());
			System.out.println("Connected to DB successfully");
			this.mongoClient = mongoClient;
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            throw me;
        }
	}
	
	public MongoClient getDBConnection () {
		if (this.mongoClient == null) {
			setDBConnection();
		}
		return this.mongoClient;
	}
	
	public void closeDBConnection () {
		this.mongoClient.close();
	}
}
