package com.usfca.cs.repository;

import java.time.LocalDateTime;
import java.util.List;

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
	List<ChatLog> findByUserIdOrderByTimestampDesc(String userId);
	List<ChatLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
	List<ChatLog> findByQuestionContainingIgnoreCase(String keyword);
	List<ChatLog> findByAnswerFound(boolean answerFound);
}