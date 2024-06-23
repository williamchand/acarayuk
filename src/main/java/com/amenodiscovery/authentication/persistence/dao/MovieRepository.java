package com.amenodiscovery.authentication.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.authentication.persistence.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByUniqueIdentifierAndGenre(String uniqueIdentifier, String genre);
    List<Movie> findByGenre(String genre);
}
