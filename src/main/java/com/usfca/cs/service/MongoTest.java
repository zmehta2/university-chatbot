package com.usfca.cs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoTest implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("MongoDB Database Name: " + mongoTemplate.getDb().getName());
        System.out.println("MongoDB Collections: " + mongoTemplate.getCollectionNames());
        
        // Manual insert
        
        mongoTemplate.getCollection("testCollection").insertOne(new org.bson.Document("name", "test"));
        System.out.println("Inserted test document. Count: " + 
                           mongoTemplate.getCollection("testCollection").countDocuments());
    }
}