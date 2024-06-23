package com.amenodiscovery.authentication.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.MovieHistory;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface MovieHistoryRepository extends JpaRepository<MovieHistory, Long> {
    List<MovieHistory> findByAccount(User account);
    Optional<MovieHistory> findByAccountAndGenreAndHistoryDate(User account, String genre, Date historyDate);
}
