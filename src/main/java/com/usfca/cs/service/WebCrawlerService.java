package com.usfca.cs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usfca.cs.model.FAQ;
import com.usfca.cs.repository.FAQRepository;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;

/**
 * This class is used to crawl the university website and update the FAQ data.
 * 
 * @author zinal
 *
 */
@Service
public class WebCrawlerService {

	private static final String BASE_URL = "https://www.usfca.edu";
	private static final int DELAY_MS = 1000; // 1 second delay between requests
	private static final String USER_AGENT = "USFCAChatbotCrawler/1.0 (+https://yourprojectwebsite.com)";

	private final FAQRepository faqRepository;
	private final Set<String> visitedUrls = new HashSet<>();
	private BaseRobotRules robotRules;

	@Autowired
	public WebCrawlerService(FAQRepository faqRepository) {
		this.faqRepository = faqRepository;
		initRobotRules();
	}

	// Initialize the robot rules by fetching and parsing robots.txt
	private void initRobotRules() {
		try {
			String robotsTxtUrl = BASE_URL + "/robots.txt";
			String robotsTxtContent = Jsoup.connect(robotsTxtUrl).execute().body();
			SimpleRobotRulesParser parser = new SimpleRobotRulesParser();
			robotRules = parser.parseContent(robotsTxtUrl, robotsTxtContent.getBytes(), "text/plain", USER_AGENT);
		} catch (IOException e) {
			// If we can't fetch robots.txt, assume nothing is allowed
			robotRules = new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_NONE);
			e.printStackTrace();
		}
	}

	// Crawl the university website and update the FAQs in MongoDB
	public void crawlAndUpdateFAQs() {
		try {
			List<FAQ> faqs = new ArrayList<>();
			crawlPage(BASE_URL + "/arts-sciences/programs/graduate/computer-science", faqs);

			if (!faqs.isEmpty()) {
				System.out.println("Attempting to save " + faqs.size() + " FAQs to MongoDB...");
				faqRepository.deleteAll();
				List<FAQ> savedFAQs = faqRepository.saveAll(faqs);
				System.out.println("Successfully saved " + savedFAQs.size() + " FAQs to MongoDB.");
			} else {
				System.out.println("No FAQs found during crawling.");
			}
		} catch (Exception e) {
			System.err.println("Error during crawling or saving FAQs: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Recursively crawl the page and its subpages
	private void crawlPage(String url, List<FAQ> faqs) throws IOException, InterruptedException {
		if (!robotRules.isAllowed(url)) {
			System.out.println("URL not allowed by robots.txt: " + url);
			return;
		}

		if (visitedUrls.contains(url)) {
			return;
		}
		visitedUrls.add(url);

		Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();

		// Extract content from the page
		extractContent(doc, faqs);

		// Find and follow links to other relevant pages
		Elements links = doc.select("a[href^=/arts-sciences/programs/graduate/computer-science]");
		for (Element link : links) {
			String absHref = link.attr("abs:href");
			if (!visitedUrls.contains(absHref)) {
				TimeUnit.MILLISECONDS.sleep(DELAY_MS); // Polite delay before next request
				crawlPage(absHref, faqs);
			}
		}
	}

	// Extract the FAQ content from the page
	private void extractContent(Document doc, List<FAQ> faqs) {
		Elements sections = doc.select("section");
		for (Element section : sections) {
			String title = section.select("h2, h3").first() != null ? section.select("h2, h3").first().text()
					: "General Information";
			Elements paragraphs = section.select("p");
			for (Element p : paragraphs) {
				if (!p.text().trim().isEmpty()) {
					FAQ faq = new FAQ();
					faq.setQuestion("What information is available about " + title + "?");
					faq.setAnswer(p.text());
					faq.setCategory(title);
					faqs.add(faq);
				}
			}
		}
	}
}