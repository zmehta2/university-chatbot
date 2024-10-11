package com.usfca.cs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usfca.cs.model.FAQ;
import com.usfca.cs.service.FAQService;
import com.usfca.cs.service.WebCrawlerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This class is used to handle the admin operations.
 * 
 * @Author zinal
 *
 */
@RestController
@RequestMapping("/api/university")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Controller", description = "Endpoints for admin operations")
@SecurityRequirement(name = "basicAuth")
public class UniversityController {

	private final FAQService faqService;
	private final WebCrawlerService crawlerService;

	@Autowired
	public UniversityController(FAQService faqService, WebCrawlerService crawlerService) {
		this.faqService = faqService;
		this.crawlerService = crawlerService;
	}

	@PostMapping("/crawl")
	public ResponseEntity<String> startCrawling() {
		try {
			crawlerService.crawlAndUpdateFAQs();
			return ResponseEntity.ok("Crawling process started successfully.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error starting crawling process: " + e.getMessage());
		}
	}

	@GetMapping("/faqs")
	public ResponseEntity<List<FAQ>> getAllFAQs() {
		return ResponseEntity.ok(faqService.getAllFAQs());
	}

	@GetMapping("/faqs/{id}")
	public ResponseEntity<FAQ> getFAQById(@PathVariable String id) {
		return faqService.getFAQById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/faqs")
	public ResponseEntity<FAQ> createFAQ(@RequestBody FAQ faq) {
		return ResponseEntity.ok(faqService.createFAQ(faq));
	}

	@PutMapping("/faqs/{id}")
	public ResponseEntity<FAQ> updateFAQ(@PathVariable String id, @RequestBody FAQ faq) {
		return ResponseEntity.ok(faqService.updateFAQ(id, faq));
	}

	@DeleteMapping("/faqs/{id}")
	public ResponseEntity<Void> deleteFAQ(@PathVariable String id) {
		faqService.deleteFAQ(id);
		return ResponseEntity.ok().build();
	}

}
