package com.amenodiscovery.authentication.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.authentication.persistence.model.ScrapeTemplate;

import java.util.List;


public interface ScrapeTemplateRepository extends JpaRepository<ScrapeTemplate, Long> {
    List<ScrapeTemplate> findByType(String type);
}
