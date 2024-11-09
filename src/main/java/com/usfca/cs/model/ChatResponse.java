package com.usfca.cs.model;

import java.util.List;

/**
 * ChatResponse is the model class for the response of the chatbot.
 * 
 * @author zinal
 *
 */
public class ChatResponse {
	private String answer;
	private List<SuggestedQuestion> suggestions;
	private String category;

	public ChatResponse(String answer, List<SuggestedQuestion> suggestions) {
		this.answer = answer;
		this.suggestions = suggestions;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public List<SuggestedQuestion> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<SuggestedQuestion> suggestions) {
		this.suggestions = suggestions;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
