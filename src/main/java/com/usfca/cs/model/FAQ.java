package com.usfca.cs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "faqs")
public class FAQ {
	@Id
	private String id;
	private String question;
	private String answer;
	private String category;
}