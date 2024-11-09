package com.usfca.cs.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usfca.cs.model.ChatLog;
import com.usfca.cs.model.ChatResponse;
import com.usfca.cs.model.FAQ;
import com.usfca.cs.model.SuggestedQuestion;
import com.usfca.cs.repository.ChatLogRepository;
import com.usfca.cs.repository.FAQRepository;

/**
 * This class is used to handle the business logic for the Chatbot model.
 * [Futureuse] This class is not used in the current implementation.
 * 
 * @author zinal
 *
 */
@Service
public class ChatbotService {
//
//	private final ConversationalChain chain;

//	public ChatbotService(@Value("${openai.api.key}") String openAiApiKey) {
//		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(openAiApiKey).build();
//
//		ChatMemory chatMemory = TokenWindowChatMemory.builder().maxTokens(1000).build();
//
//		this.chain = ConversationalChain.builder().chatLanguageModel(model).chatMemory(chatMemory).build();

//	}

//	public String processMessage(String message) {
//		return chain.execute(message);
//	}

	private final FAQRepository faqRepository;
	private final ChatLogRepository chatLogRepository;

	@Autowired
	public ChatbotService(FAQRepository faqRepository, ChatLogRepository chatLogRepository) {
		this.faqRepository = faqRepository;
		this.chatLogRepository = chatLogRepository;
	}

	public ChatResponse processQuery(String question, String userId) {
		// Convert question to lowercase for better matching
		String lowercaseQuestion = question.toLowerCase();

		// Search for relevant FAQs
		List<FAQ> relevantFaqs = findRelevantFAQs(lowercaseQuestion);

		// Log the interaction
		logInteraction(userId, question, relevantFaqs);

		// Build response
		return buildResponse(relevantFaqs, question);
	}

	private List<FAQ> findRelevantFAQs(String question) {
		// Split the question into keywords
		Set<String> keywords = extractKeywords(question);

		// Search for FAQs containing these keywords
		return faqRepository.findAll().stream().filter(faq -> isRelevant(faq, keywords))
				.sorted((f1, f2) -> calculateRelevance(f2, keywords) - calculateRelevance(f1, keywords)).limit(3) // Get
																													// top
																													// 3
																													// most
																													// relevant
																													// answers
				.collect(Collectors.toList());
	}

	private Set<String> extractKeywords(String question) {
		// Remove common words and extract important keywords
		List<String> stopWords = Arrays.asList("what", "where", "when", "how", "is", "are", "the", "a", "an", "and",
				"or", "but");
		return Arrays.stream(question.toLowerCase().split("\\s+")).filter(word -> !stopWords.contains(word))
				.collect(Collectors.toSet());
	}

	private boolean isRelevant(FAQ faq, Set<String> keywords) {
		String content = (faq.getQuestion() + " " + faq.getAnswer()).toLowerCase();
		return keywords.stream().anyMatch(content::contains);
	}

	private int calculateRelevance(FAQ faq, Set<String> keywords) {
		String content = (faq.getQuestion() + " " + faq.getAnswer()).toLowerCase();
		return (int) keywords.stream().filter(content::contains).count();
	}

	private ChatResponse buildResponse(List<FAQ> relevantFaqs, String originalQuestion) {
		if (relevantFaqs.isEmpty()) {
			return new ChatResponse(
					"I couldn't find a specific answer to your question. "
							+ "Please try rephrasing or contact the university support for more information.",
					Collections.emptyList());
		}

		String mainAnswer = relevantFaqs.get(0).getAnswer();

		// If we have multiple relevant FAQs, suggest them
		List<SuggestedQuestion> suggestions = relevantFaqs.stream().skip(1) // Skip the main answer
				.map(faq -> new SuggestedQuestion(faq.getQuestion(), faq.getCategory())).collect(Collectors.toList());

		return new ChatResponse(mainAnswer, suggestions);
	}

	private void logInteraction(String userId, String question, List<FAQ> relevantFaqs) {
		ChatLog log = new ChatLog();
		log.setUserId(userId);
		log.setQuestion(question);
		log.setAnswerFound(!relevantFaqs.isEmpty());
		log.setTimestamp(LocalDateTime.now());
		chatLogRepository.save(log);
	}

	// Method to update feedback
	public void updateFeedback(String chatLogId, boolean helpful, String feedback) {
		chatLogRepository.findById(chatLogId).ifPresent(chatLog -> {
			chatLog.setHelpful(helpful);
			chatLog.setUserFeedback(feedback);
			chatLogRepository.save(chatLog);
		});
	}

	// Method to get user's chat history
	public List<ChatLog> getUserHistory(String userId) {
		return chatLogRepository.findByUserIdOrderByTimestampDesc(userId);
	}

	public void saveUserHistory(ChatLog chatLog) {
		ChatLog log = new ChatLog();
		log.setUserId(chatLog.getUserId());
		log.setQuestion(chatLog.getQuestion());
		log.setAnswer(chatLog.getAnswer());
		log.setAnswerFound(chatLog.isAnswerFound());
		log.setCategory(chatLog.getCategory());
		log.setTimestamp(LocalDateTime.now());
		log.setSource(chatLog.getSource());
		log.setHelpful(chatLog.isHelpful());
		log.setUserFeedback(chatLog.getUserFeedback());
		chatLogRepository.save(log);

	}
}
