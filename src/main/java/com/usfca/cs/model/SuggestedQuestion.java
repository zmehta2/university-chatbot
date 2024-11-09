package com.usfca.cs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * SuggestedQuestion is the model class for suggested questions.
 * 
 * @author zinal
 *
 */
@Getter
@Setter
public class SuggestedQuestion {

	private String question;
	private String category;

	public SuggestedQuestion(String question, String category) {
		this.question = question;
		this.category = category;
	}

}
