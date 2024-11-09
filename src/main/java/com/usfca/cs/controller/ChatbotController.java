package com.usfca.cs.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usfca.cs.model.ChatLog;
import com.usfca.cs.model.FeedbackRequest;
import com.usfca.cs.repository.ChatLogRepository;
import com.usfca.cs.service.ChatbotService;

/**
 * This class is used to handle the chatbot requests. It provides endpoints for
 * chat history, feedback, and analytics.
 * 
 * @author zinal
 *
 */
@RestController
@RequestMapping("/api/chat-history")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatbotController {

	private final ChatbotService chatbotService;
	private final ChatLogRepository chatLogRepository;

	@Autowired
	public ChatbotController(ChatbotService chatbotService, ChatLogRepository chatLogRepository) {
		this.chatbotService = chatbotService;
		this.chatLogRepository = chatLogRepository;
	}

	@GetMapping("/user/{userId}")
	public List<ChatLog> getUserHistory(@PathVariable String userId) {
		return chatLogRepository.findByUserIdOrderByTimestampDesc(userId);
	}

	@PostMapping("/user")
	public ResponseEntity<?> saveUserHistory(@RequestBody ChatLog chatLog) {
		chatbotService.saveUserHistory(chatLog);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/feedback/{chatLogId}")
	public ResponseEntity<?> provideFeedback(@PathVariable String chatLogId, @RequestBody FeedbackRequest feedback) {
		chatbotService.updateFeedback(chatLogId, feedback.isHelpful(), feedback.getFeedback());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/analytics/popular-questions")
	public Map<String, Long> getPopularQuestions() {
		List<ChatLog> allLogs = chatLogRepository.findAll();
		return allLogs.stream().collect(Collectors.groupingBy(ChatLog::getQuestion, Collectors.counting()));
	}

	@GetMapping("/chat-logs/category-analytics")
	public Map<String, Long> getCategoryAnalytics() {
		List<ChatLog> logs = chatLogRepository.findAll();
		return logs.stream().filter(c -> c.getCategory() != null)
				.collect(Collectors.groupingBy(ChatLog::getCategory, Collectors.counting()));
	}

}
