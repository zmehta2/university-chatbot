package com.usfca.cs.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * ChatLog is the model class for chat logs.
 * 
 * @Author Zinal
 *
 */
@Data
@Document(collection = "chat_logs")
public class ChatLog {
	@Id
	private String id;
	private String userId;
	private String question;
	private String answer;
	private boolean answerFound;
	private String category;
	private LocalDateTime timestamp;
	private String source; // FAQ or Crawled
	private boolean helpful; // User feedback
	private String userFeedback; // Additional feedback

}