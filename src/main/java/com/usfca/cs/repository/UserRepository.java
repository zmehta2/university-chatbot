package com.usfca.cs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.usfca.cs.model.User;

/**
 * UserRepository is the repository interface for the User model.
 * 
 * @author zinal
 *
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);
}