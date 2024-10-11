package com.usfca.cs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * MongoConfig is the configuration class for MongoDB.
 * 
 * @author zinal
 *
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Override
	protected String getDatabaseName() {
		return "universitychatbot";
	}
}