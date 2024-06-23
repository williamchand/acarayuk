package com.amenodiscovery.authentication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amenodiscovery.authentication.persistence.model.ScrapeTemplate;
import com.amenodiscovery.authentication.persistence.dao.ScrapeTemplateRepository;

@Service
public class ScrapeTemplateService {
    
    private final ScrapeTemplateRepository scrapeTemplateRepository;

    public ScrapeTemplateService(ScrapeTemplateRepository scrapeTemplateRepository) {
        this.scrapeTemplateRepository = scrapeTemplateRepository;
    }

    public List<ScrapeTemplate> getScrapeTemplates(String type) {
        return scrapeTemplateRepository.findByType(type);
    }
}
