package com.amenodiscovery.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.movies.entity.ScrapeTemplate;

import java.util.List;


public interface ScrapeTemplateRepository extends JpaRepository<ScrapeTemplate, Long> {
    List<ScrapeTemplate> findByType(String type);
}
