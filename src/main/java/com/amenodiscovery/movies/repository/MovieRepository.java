package com.amenodiscovery.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.movies.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByUniqueIdentifierAndGenre(String uniqueIdentifier, String genre);
    List<Movie> findByGenre(String genre);
}
