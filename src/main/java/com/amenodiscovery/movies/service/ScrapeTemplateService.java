package com.amenodiscovery.movies.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amenodiscovery.movies.entity.ScrapeTemplate;
import com.amenodiscovery.movies.repository.ScrapeTemplateRepository;

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
