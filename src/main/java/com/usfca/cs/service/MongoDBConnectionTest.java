package com.usfca.cs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * MongoDBConnectionTest is a service class to test the MongoDB connection.
 * 
 * @Author Zinal
 *
 */
@Component
public class MongoDBConnectionTest implements CommandLineRunner {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("MongoDB Database Name: " + mongoTemplate.getDb().getName());
		System.out.println("MongoDB Collections: " + mongoTemplate.getCollectionNames());
	}
}