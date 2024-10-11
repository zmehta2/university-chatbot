package com.usfca.cs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usfca.cs.model.FAQ;
import com.usfca.cs.repository.FAQRepository;

/**
 * This class is used to handle the business logic for the FAQ model.
 * 
 * @author zinal
 *
 */

@Service
public class FAQService {

	private final FAQRepository faqRepository;

    @Autowired
    public FAQService(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }

    public Optional<FAQ> getFAQById(String id) {
        return faqRepository.findById(id);
    }

    public FAQ createFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }

    public FAQ updateFAQ(String id, FAQ faq) {
        faq.setId(id);
        return faqRepository.save(faq);
    }

    public void deleteFAQ(String id) {
        faqRepository.deleteById(id);
    }
	
}
