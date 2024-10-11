package com.usfca.cs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usfca.cs.service.ChatbotService;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

	@Autowired
	private ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String message) {
//        String response = chatbotService.processMessage(message);
//        return ResponseEntity.ok(response);
    	return ResponseEntity.ok("response");
    }
	
}
