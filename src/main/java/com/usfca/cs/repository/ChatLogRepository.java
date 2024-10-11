package com.usfca.cs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.usfca.cs.model.ChatLog;

/**
 * ChatLogRepository is the repository interface for the ChatLog model.
 * 
 * @author zinal
 *
 */
@Repository
public interface ChatLogRepository extends MongoRepository<ChatLog, String> {

}