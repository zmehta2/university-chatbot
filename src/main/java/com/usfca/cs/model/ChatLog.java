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
	private String message;
	private String response;
	private LocalDateTime timestamp;
}