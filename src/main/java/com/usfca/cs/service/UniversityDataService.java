package com.usfca.cs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.usfca.cs.model.FAQ;
import com.usfca.cs.repository.FAQRepository;

/**
 * This class is used to scrape the university website and update the FAQ data.
 * * [Futureuse] This class is not used in the current implementation. It is
 * intended to be used in future versions of the project.
 * 
 * @author zinal
 *
 */
@Service
public class UniversityDataService {

	private static final Logger LOGGER = Logger.getLogger(UniversityDataService.class.getName());
	private static final String BASE_URL = "https://www.usfca.edu/arts-sciences/programs/graduate/computer-science";

	private final FAQRepository faqRepository;

	@Autowired
	public UniversityDataService(FAQRepository faqRepository) {
		this.faqRepository = faqRepository;
	}

	@Scheduled(cron = "0 0 1 * * ?") // Run every day at 1 AM
	public void updateUniversityData() {
		try {
			List<FAQ> faqs = scrapeUniversityWebsite();
			faqRepository.saveAll(faqs);
			LOGGER.info("Successfully updated FAQs from university website");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error updating university data", e);
		}
	}

	private List<FAQ> scrapeUniversityWebsite() throws IOException {
		List<FAQ> faqs = new ArrayList<>();

		// Fetch the main page
		Document doc = fetchPage(BASE_URL);
		if (doc != null) {
			extractFAQs(doc, "General", faqs);
		}

		// Attempt to fetch additional pages
		String[] additionalPages = { "/admissions", "/curriculum", "/faculty", "/careers" };
		for (String page : additionalPages) {
			try {
				Document subDoc = fetchPage(BASE_URL + page);
				if (subDoc != null) {
					extractFAQs(subDoc, page.substring(1), faqs);
				}
			} catch (HttpStatusException e) {
				LOGGER.warning("Failed to fetch page: " + page + ". Error: " + e.getMessage());
			}
		}

		return faqs;
	}

	private Document fetchPage(String url) throws IOException {
		try {
			return Jsoup.connect(url).get();
		} catch (HttpStatusException e) {
			LOGGER.warning("Failed to fetch page: " + url + ". Error: " + e.getMessage());
			return null;
		}
	}

	private void extractFAQs(Document doc, String category, List<FAQ> faqs) {
		Elements faqElements = doc.select(".faq-item, .info-item");
		for (Element element : faqElements) {
			String question = element.select(".faq-question, .info-title").text();
			String answer = element.select(".faq-answer, .info-content").text();
			if (!question.isEmpty() && !answer.isEmpty()) {
				FAQ faq = new FAQ();
				faq.setQuestion(question);
				faq.setAnswer(answer);
				faq.setCategory(category);
				faqs.add(faq);
			}
		}
	}
}