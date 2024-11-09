package com.usfca.cs.repository;

import java.util.List;

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

	List<FAQ> findByQuestionContainingIgnoreCase(String keyword); // Search by keyword in question field

	List<FAQ> findByCategoryIgnoreCase(String category);
	
}