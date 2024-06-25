package com.amenodiscovery.authentication.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.authentication.persistence.model.AnimalHistory;

import java.util.Date;
import java.util.Optional;


public interface AnimalHistoryRepository extends JpaRepository<AnimalHistory, Long> {
    Optional<AnimalHistory> findByNameAndHistoryDate(String name, Date historyDate);
}
