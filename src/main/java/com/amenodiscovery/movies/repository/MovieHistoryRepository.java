package com.amenodiscovery.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.movies.entity.Account;
import com.amenodiscovery.movies.entity.MovieHistory;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface MovieHistoryRepository extends JpaRepository<MovieHistory, Long> {
    List<MovieHistory> findByAccount(Account account);
    Optional<MovieHistory> findByAccountAndGenreAndHistoryDate(Account account, String genre, Date historyDate);
}
