package com.usfca.cs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.usfca.cs.model.FAQ;

/**
 * FAQRepository is the repository interface for the FAQ model.
 * 
 * @author Zinal
 *
 */
@Repository
public interface FAQRepository extends MongoRepository<FAQ, String> {

}